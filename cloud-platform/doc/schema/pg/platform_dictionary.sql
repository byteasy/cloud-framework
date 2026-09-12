-- 字典定义表
CREATE TABLE platform_dictionary1 (
    -- 主键
                               id BIGSERIAL PRIMARY KEY,
    -- 分组编号
                               group_id BIGINT DEFAULT NULL,
    -- 编码
                               code VARCHAR(64) NOT NULL,
    -- 名称
                               name VARCHAR(128) NOT NULL,
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
-- ========== platform_dictionary 表字段注释 ==========
COMMENT ON COLUMN platform_dictionary1.id IS '主键';
COMMENT ON COLUMN platform_dictionary1.group_id IS '分组编号';
COMMENT ON COLUMN platform_dictionary1.code IS '编码';
COMMENT ON COLUMN platform_dictionary1.name IS '名称';
COMMENT ON COLUMN platform_dictionary1.status IS '状态';
COMMENT ON COLUMN platform_dictionary1.remark IS '备注';
COMMENT ON COLUMN platform_dictionary1.creator_id IS '创建人账户编号';
COMMENT ON COLUMN platform_dictionary1.created_by IS '创建人姓名';
COMMENT ON COLUMN platform_dictionary1.created_time IS '创建时间';
COMMENT ON COLUMN platform_dictionary1.updater_id IS '更新人账户编号';
COMMENT ON COLUMN platform_dictionary1.updated_by IS '更新人姓名';
COMMENT ON COLUMN platform_dictionary1.updated_time IS '更新时间';
COMMENT ON COLUMN platform_dictionary1.version IS '乐观锁版本号';

-- ========== 表注释 ==========
COMMENT ON TABLE platform_dictionary1 IS '字典表';

-- 1. 资源编码唯一索引
CREATE UNIQUE INDEX IF NOT EXISTS uk_platform_dictionary_code1 ON platform_dictionary1(code);

