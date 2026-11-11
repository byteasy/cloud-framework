-- 1. 创建统一搜索索引表
CREATE TABLE platform_search_index
(
    class_name    VARCHAR(64)  NOT NULL,  -- 实体类型（如 Product, Article, Order）
    entity_id     VARCHAR(64)  NOT NULL,  -- 业务主键 ID
    entity_code   VARCHAR(128) NOT NULL,  -- 业务单号/编码
    title         VARCHAR(512),           -- 检索展示主标题
    summary       TEXT,                   -- 检索展示摘要
    search_vector tsvector,               -- 预计算全文索引向量
    updated_time  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (class_name, entity_id)
);

-- 2. 核心倒排索引（加速全文检索）
CREATE INDEX idx_search_vector ON platform_search_index USING GIN(search_vector);
ALTER INDEX idx_search_vector SET (fastupdate = on, gin_pending_list_limit = 65536);
-- 3. 辅助索引：支持根据实体类型过滤并按更新时间倒序展示
CREATE INDEX idx_search_class_updated ON platform_search_index(class_name, updated_time DESC);


-- 4. 优化后的生产检索 SQL（带防御与高效高亮）
SELECT
    class_name,
    entity_id,
    entity_code,
    title,
    summary,
    -- 权重得分计算
    ts_rank(search_vector, query) AS score,
    -- 原生 HTML 高亮
    ts_headline('simple', coalesce(summary, title), query, 'StartSel=<em>, StopSel=</em>, MaxWords=50, MinWords=15') AS highlight_summary
FROM
    platform_search_index,
    -- 采用 websearch_to_tsquery 防止用户输入特殊字符引起 SQL 语法崩溃
    websearch_to_tsquery('simple', :keyword) query
WHERE
    search_vector @@ query
-- 可选：过滤特定业务实体
-- AND class_name = :className
ORDER BY
    score DESC,
    updated_time DESC
    LIMIT :pageSize OFFSET :offset;


-- 5. 插入 / 幂等更新 SQL
INSERT INTO platform_search_index (
    class_name,
    entity_id,
    entity_code,
    title,
    summary,
    search_vector,
    updated_time
) VALUES (
             :className,
             :entityId,
             :entityCode,
             :title,
             :summary,
             -- 多字段不同权重拼接（A:高 -> D:低）
             setweight(to_tsvector('simple', coalesce(:title, '')), 'A') ||
             setweight(to_tsvector('simple', coalesce(:entityCode, '')), 'A') ||
             setweight(to_tsvector('simple', coalesce(:categoryName, '')), 'B') ||
             setweight(to_tsvector('simple', coalesce(:summary, '')), 'B') ||
             setweight(to_tsvector('simple', coalesce(:specsDetail, '')), 'C') ||
             setweight(to_tsvector('simple', coalesce(:remark, '')), 'D'),
             CURRENT_TIMESTAMP
         )
    ON CONFLICT (class_name, entity_id)
DO UPDATE SET
    entity_code   = EXCLUDED.entity_code,   -- 补齐编码更新
           title         = EXCLUDED.title,
           summary       = EXCLUDED.summary,
           search_vector = EXCLUDED.search_vector,
           updated_time  = CURRENT_TIMESTAMP;