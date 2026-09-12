-- 平台资源授权表（记录OAuth2客户端可以访问哪些平台资源）
CREATE TABLE platform_resource_openapi
(
    resource_id BIGINT NOT NULL,
    openapi_id BIGINT NOT NULL,
    -- 复合主键：一个客户端对同一个资源只能有一条授权记录
    PRIMARY KEY (resource_id, openapi_id),
    -- 审计字段
    creator_id                  VARCHAR(64) NOT NULL, -- 创建人账户ID（来自IAM等）
    created_by                  VARCHAR(64) NOT NULL, -- 创建人显示名称/用户ID
    created_time                TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- 创建索引以提高查询性能
CREATE INDEX idx_platform_resource_openapi_resource_id
    ON platform_resource_openapi (resource_id);
CREATE INDEX idx_platform_resource_openapi_openapi_id
    ON platform_resource_openapi (openapi_id);

-- 添加注释（可选但推荐）
COMMENT
ON TABLE platform_resource_openapi IS 'OAuth2客户端与平台资源的授权关联表';
COMMENT
ON COLUMN platform_resource_openapi.resource_id IS 'OAuth2客户端ID，引用oauth2_registered_client表';
COMMENT
ON COLUMN platform_resource_openapi.openapi_id IS '平台资源ID，引用openapi_id';
COMMENT
ON COLUMN platform_resource_openapi.creator_id IS '创建人账户ID（如：工号/账号）';
COMMENT
ON COLUMN platform_resource_openapi.created_by IS '创建人名称';
COMMENT
ON COLUMN platform_resource_openapi.created_time IS '创建时间';