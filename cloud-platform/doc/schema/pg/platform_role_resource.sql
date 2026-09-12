CREATE TABLE platform_role_resource
(
    id           BIGSERIAL PRIMARY KEY,
    role_id      BIGINT      NOT NULL,
    resource_id  BIGINT      NOT NULL,
    -- 审计字段
    creator_id   VARCHAR(64) NOT NULL, -- 创建人账户ID（来自IAM等）
    created_by   VARCHAR(64) NOT NULL, -- 创建人显示名称/用户ID
    created_time TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 添加注释（可选但推荐）
COMMENT ON TABLE platform_role_resource IS '平台资源角色授权表';
COMMENT ON COLUMN platform_role_resource.id IS '主键';
COMMENT ON COLUMN platform_role_resource.role_id IS '平台资源关联的角色ID';
COMMENT ON COLUMN platform_role_resource.resource_id IS '平台资源ID';
COMMENT ON COLUMN platform_role_resource.creator_id IS '创建人账户ID（如：工号/账号）';
COMMENT ON COLUMN platform_role_resource.created_by IS '创建人名称';
COMMENT ON COLUMN platform_role_resource.created_time IS '创建时间';

-- 约束
ALTER TABLE platform_role_resource
    ADD CONSTRAINT fk_platform_role_resource_role_id
        FOREIGN KEY (role_id)
            REFERENCES platform_role(id) ON DELETE CASCADE;

ALTER TABLE platform_role_resource
    ADD CONSTRAINT fk_platform_role_resource_resource_id
        FOREIGN KEY (resource_id)
            REFERENCES platform_resource(id) ON DELETE CASCADE;

-- 创建索引以提高查询性能
CREATE INDEX idx_platform_role_resource_role_id ON platform_role_resource (role_id);
CREATE INDEX idx_platform_role_resource_resource_id ON platform_role_resource (resource_id);

