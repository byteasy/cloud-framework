-- 平台岗位授权表
CREATE TABLE platform_job_resource
(
    job_id BIGINT NOT NULL,
    resource_id BIGINT NOT NULL,
    -- 复合主键：一个客户端对同一个资源只能有一条授权记录
    PRIMARY KEY (job_id, resource_id),
    -- 审计字段
    creator_id                  VARCHAR(64) NOT NULL, -- 创建人账户ID（来自IAM等）
    created_by                  VARCHAR(64) NOT NULL, -- 创建人显示名称/用户ID
    created_time                TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);



-- 添加注释（可选但推荐）
COMMENT ON TABLE platform_job_resource IS '平台用户分组授权表';
COMMENT ON COLUMN platform_job_resource.job_id IS '平台用户分组ID';
COMMENT ON COLUMN platform_job_resource.resource_id IS '平台资源ID';
COMMENT
ON COLUMN platform_job_resource.creator_id IS '创建人账户ID（如：工号/账号）';
COMMENT
ON COLUMN platform_job_resource.created_by IS '创建人名称';
COMMENT
ON COLUMN platform_job_resource.created_time IS '创建时间';