-- 字典项目定义表
CREATE TABLE platform_dictionary_item (
    -- 主键
                                     id BIGSERIAL PRIMARY KEY,
    -- 字典
                                     dictionary_id BIGINT NOT NULL,
    -- 编码
                                     code VARCHAR(64) NOT NULL,
    -- 标签
                                     label VARCHAR(128) NOT NULL,
    -- 值
                                     value VARCHAR(128) NOT NULL,
    -- 是否默认
                                     is_default SMALLINT NOT NULL DEFAULT 0,
    -- 显示样式
                                     css VARCHAR(128) DEFAULT NULL,
    -- 显示顺序
                                     show_order SMALLINT NOT NULL DEFAULT 1,
    -- 状态
                                     status SMALLINT NOT NULL DEFAULT 1,
    -- 备注
                                     remark VARCHAR(512) DEFAULT NULL,
    -- 审计字段：创建人账户ID
                                     creator_id VARCHAR(64) NOT NULL,
    -- 审计字段：创建人（用户ID）
                                     created_by VARCHAR(64) NOT NULL,
    -- 审计字段：创建时间
                                     created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- 审计字段：更新人账户ID
                                     updater_id VARCHAR(64) DEFAULT NULL,
    -- 审计字段：更新人姓名
                                     updated_by VARCHAR(64) DEFAULT NULL,
    -- 审计字段：更新时间
                                     updated_time TIMESTAMP DEFAULT NULL,
    -- 乐观锁
                                     version INTEGER DEFAULT 1
);
-- ========== platform_dictionary_item 表字段注释 ==========
COMMENT ON COLUMN platform_dictionary_item.id IS '主键';
COMMENT ON COLUMN platform_dictionary_item.dictionary_id IS '字典';
COMMENT ON COLUMN platform_dictionary_item.code IS '编码';
COMMENT ON COLUMN platform_dictionary_item.label IS '标签';
COMMENT ON COLUMN platform_dictionary_item.value IS '值';
COMMENT ON COLUMN platform_dictionary_item.is_default IS '是否默认';
COMMENT ON COLUMN platform_dictionary_item.css IS '显示样式';
COMMENT ON COLUMN platform_dictionary_item.show_order IS '显示顺序';
COMMENT ON COLUMN platform_dictionary_item.status IS '状态';
COMMENT ON COLUMN platform_dictionary_item.remark IS '备注';
COMMENT ON COLUMN platform_dictionary_item.creator_id IS '创建人账户ID';
COMMENT ON COLUMN platform_dictionary_item.created_by IS '创建人姓名';
COMMENT ON COLUMN platform_dictionary_item.created_time IS '创建时间';
COMMENT ON COLUMN platform_dictionary_item.updater_id IS '更新人账户ID';
COMMENT ON COLUMN platform_dictionary_item.updated_by IS '更新人姓名';
COMMENT ON COLUMN platform_dictionary_item.updated_time IS '更新时间';
COMMENT ON COLUMN platform_dictionary_item.version IS '乐观锁版本号';

-- ========== 表注释 ==========
COMMENT ON TABLE platform_dictionary_item IS '字典项目定义表';

ALTER TABLE platform_dictionary_item
    ADD CONSTRAINT fk_platform_dictionary_item_dictionary_id_restrict
        FOREIGN KEY (dictionary_id)
            REFERENCES platform_dictionary(id)
            ON UPDATE CASCADE
            ON DELETE RESTRICT;

