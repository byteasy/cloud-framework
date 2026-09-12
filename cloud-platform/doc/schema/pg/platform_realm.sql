-- 平台用户领域定义表（平台级）
CREATE TABLE platform_realm (
    -- 主键（自增ID，内部使用）
                               id BIGSERIAL PRIMARY KEY,
    -- 分组编码(需创建唯一索引)
                               code VARCHAR(64) NOT NULL,
    -- 分组名称
                               name VARCHAR(128) NOT NULL,
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
-- ========== platform_resource_group 表字段注释 ==========
COMMENT ON COLUMN platform_realm.id IS '主键（自增ID，内部使用）';
COMMENT ON COLUMN platform_realm.code IS '分组编码（业务唯一标识，用于关联资源）';
COMMENT ON COLUMN platform_realm.name IS '分组名称';
COMMENT ON COLUMN platform_realm.remark IS '备注';
COMMENT ON COLUMN platform_realm.creator_id IS '创建人账户ID';
COMMENT ON COLUMN platform_realm.created_by IS '创建人姓名';
COMMENT ON COLUMN platform_realm.created_time IS '创建时间';
COMMENT ON COLUMN platform_realm.updater_id IS '更新人账户ID';
COMMENT ON COLUMN platform_realm.updated_by IS '更新人姓名';
COMMENT ON COLUMN platform_realm.updated_time IS '更新时间';
COMMENT ON COLUMN platform_realm.version IS '乐观锁版本号';

-- ========== 表注释 ==========
COMMENT ON TABLE platform_realm IS '平台用户领域定义表';

-- ========== 建议添加的索引 ==========

-- 1. 分组编码唯一索引（业务主键）
CREATE UNIQUE INDEX IF NOT EXISTS uk_platform_realm_code ON platform_realm(code);

