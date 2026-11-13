CREATE EXTENSION IF NOT EXISTS btree_gin;
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- 1. 建表（修正了 TIMESTAMPT -> TIMESTAMPTZ）
CREATE TABLE platform_search_index
(
    category_id   BIGINT       NOT NULL,   -- 一级领域分类（对应顶部Tabs：图片/文档/流程/人员等）
    group_id      BIGINT       NOT NULL,   -- 子领域/空间分组
    class_name    VARCHAR(64)  NOT NULL,   -- 实体类型
    entity_id     VARCHAR(64)  NOT NULL,   -- 业务主键 ID
    entity_code   VARCHAR(128) NOT NULL,   -- 业务单号/编码
    title         VARCHAR(512),            -- 检索展示主标题
    summary       TEXT,                    -- 检索展示摘要
    search_vector tsvector,                -- 预计算全文索引向量
    updated_by    VARCHAR(64)  DEFAULT NULL,
    updated_time  TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP,
    created_by    VARCHAR(64)  NOT NULL,
    created_time  TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (class_name, entity_id)    -- 已自动包含 (class_name, entity_id) 唯一索引
);

-- 2. 核心全文检索倒排索引（“全部”Tab + “相关度优先”）
CREATE INDEX idx_search_vector ON platform_search_index USING GIN(search_vector);
ALTER INDEX idx_search_vector SET (fastupdate = on, gin_pending_list_limit = 65536);

-- 3. 分类 + 全文联合 GIN 索引（“按人员/按文档”及各分类Tab搜索、首页分类预览）
CREATE INDEX idx_search_cat_vector ON platform_search_index USING GIN(category_id, search_vector);
ALTER INDEX idx_search_cat_vector SET (fastupdate = on, gin_pending_list_limit = 65536);

-- 4. 分类 + 时间排序索引（支撑“最近更新”、“最新创建”、“最早创建”）
CREATE INDEX idx_search_cat_updated ON platform_search_index(category_id, updated_time DESC);
CREATE INDEX idx_search_cat_created ON platform_search_index(category_id, created_time DESC);

-- 5. 全局空搜兜底排序索引（不限分类且无关键词时，直接翻页不走全表扫描）
CREATE INDEX idx_search_updated_time ON platform_search_index(updated_time DESC);

-- 6. 标题三元组模糊索引（支撑“按标题”及长文件名/编号的任意子串 ILIKE '%xxx%' 检索）
CREATE INDEX idx_search_title_trgm ON platform_search_index USING GIN(title gin_trgm_ops);