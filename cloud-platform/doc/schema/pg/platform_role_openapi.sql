CREATE TABLE platform_role_openapi
(
    id           BIGSERIAL PRIMARY KEY,
    role_id      BIGINT      NOT NULL,
    openapi_id   BIGINT      NOT NULL,
    creator_id   VARCHAR(64) NOT NULL,
    created_by   VARCHAR(64) NOT NULL,
    created_time TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 添加注释（可选但推荐）
COMMENT ON TABLE platform_role_openapi IS '角色接口授权表';
COMMENT ON COLUMN platform_role_openapi.id IS '编号';
COMMENT ON COLUMN platform_role_openapi.role_id IS '平台资源关联的角色ID';
COMMENT ON COLUMN platform_role_openapi.openapi_id IS '平台资源ID';
COMMENT ON COLUMN platform_role_openapi.creator_id IS '创建人账户ID（如：工号/账号）';
COMMENT ON COLUMN platform_role_openapi.created_by IS '创建人名称';
COMMENT ON COLUMN platform_role_openapi.created_time IS '创建时间';

-- 约束
ALTER TABLE platform_role_openapi
    ADD CONSTRAINT fk_platform_role_openapi_role_id
        FOREIGN KEY (role_id)
            REFERENCES platform_role(id) ON DELETE CASCADE;

ALTER TABLE platform_role_openapi
    ADD CONSTRAINT fk_platform_role_openapi_openapi_id
        FOREIGN KEY (openapi_id)
            REFERENCES platform_openapi(id) ON DELETE CASCADE;