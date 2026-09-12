-- 平台用户分组表
CREATE TABLE platform_user_group_user
(
    user_id BIGINT NOT NULL,
    group_id BIGINT NOT NULL,
    -- 复合主键：一个客户端对同一个资源只能有一条授权记录
    PRIMARY KEY (user_id, group_id),
    -- 审计字段
    creator_id                  VARCHAR(64) NOT NULL, -- 创建人账户ID（来自IAM等）
    created_by                  VARCHAR(64) NOT NULL, -- 创建人显示名称/用户ID
    created_time                TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- 创建索引以提高查询性能
CREATE INDEX idx_platform_user_group_user_user_id
    ON platform_user_group_user (user_id);
CREATE INDEX idx_platform_user_group_user_group_id
    ON platform_user_group (group_id);

-- 添加注释（可选但推荐）
COMMENT ON TABLE platform_user_group_user IS '平台用户分组表';
COMMENT ON COLUMN platform_user_group_user.role_id IS '平台资源关联的角色ID';
COMMENT ON COLUMN platform_user_group_user.resource_id IS '平台资源ID';
COMMENT
ON COLUMN platform_user_group_user.creator_id IS '创建人账户ID（如：工号/账号）';
COMMENT
ON COLUMN platform_user_group_user.created_by IS '创建人名称';
COMMENT
ON COLUMN platform_user_group_user.created_time IS '创建时间';