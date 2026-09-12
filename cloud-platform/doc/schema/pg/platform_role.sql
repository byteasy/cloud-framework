-- 平台角色定义表（平台级）
CREATE TABLE platform_role (
    -- 主键
                               id BIGSERIAL PRIMARY KEY,
    -- 编码
                               code VARCHAR(64) NOT NULL,
    -- 状态
                               status SMALLINT NOT NULL DEFAULT 1,
    -- 名称
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

-- ========== platform_service 表字段注释 ==========
COMMENT ON COLUMN platform_role.id IS '主键';
COMMENT ON COLUMN platform_role.code IS '编码';
COMMENT ON COLUMN platform_role.status IS '状态';
COMMENT ON COLUMN platform_role.name IS '名称';
COMMENT ON COLUMN platform_role.remark IS '备注';
COMMENT ON COLUMN platform_role.creator_id IS '创建人账户ID';
COMMENT ON COLUMN platform_role.created_by IS '创建人姓名';
COMMENT ON COLUMN platform_role.created_time IS '创建时间';
COMMENT ON COLUMN platform_role.updater_id IS '更新人账户ID';
COMMENT ON COLUMN platform_role.updated_by IS '更新人姓名';
COMMENT ON COLUMN platform_role.updated_time IS '更新时间';
COMMENT ON COLUMN platform_role.version IS '乐观锁版本号';

-- ========== 表注释 ==========
COMMENT ON TABLE platform_role IS '平台角色定义表';

ALTER TABLE platform_role
    ADD CONSTRAINT uk_platform_role_code UNIQUE (code);