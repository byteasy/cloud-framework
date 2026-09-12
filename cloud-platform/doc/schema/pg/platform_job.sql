-- 平台岗位定义表（平台级）
CREATE TABLE platform_job (
    -- 主键（自增ID，内部使用）
                               id BIGSERIAL PRIMARY KEY,
    -- 编码(需创建唯一索引)
                               code VARCHAR(64) NOT NULL,
    -- 组织编码
                               org_id BIGINT NOT NULL,
    -- 状态（0-禁用，1-正常）
                               status SMALLINT NOT NULL DEFAULT 1,
    -- 名称
                               name VARCHAR(128) NOT NULL,
    -- 备注
                               remark VARCHAR(512) DEFAULT NULL,
    -- 显示顺序，值越小，越靠前
                               show_order SMALLINT NOT NULL DEFAULT 1,
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

-- ========== platform_job 表字段注释 ==========
COMMENT ON COLUMN platform_job.id IS '主键（自增ID，内部使用）';
COMMENT ON COLUMN platform_job.code IS '编码（业务唯一标识）';
COMMENT ON COLUMN platform_job.org_id IS '组织编码';
COMMENT ON COLUMN platform_job.status IS '状态：0-禁用，1-正常';
COMMENT ON COLUMN platform_job.name IS '名称';
COMMENT ON COLUMN platform_job.remark IS '备注';
COMMENT ON COLUMN platform_job.show_order IS '显示顺序，值越小，越靠前';
COMMENT ON COLUMN platform_job.creator_id IS '创建人账户ID';
COMMENT ON COLUMN platform_job.created_by IS '创建人姓名';
COMMENT ON COLUMN platform_job.created_time IS '创建时间';
COMMENT ON COLUMN platform_job.updater_id IS '更新人账户ID';
COMMENT ON COLUMN platform_job.updated_by IS '更新人姓名';
COMMENT ON COLUMN platform_job.updated_time IS '更新时间';
COMMENT ON COLUMN platform_job.version IS '乐观锁版本号';

-- ========== 表注释 ==========
COMMENT ON TABLE platform_job IS '平台岗位定义表';

ALTER TABLE platform_job
    ADD CONSTRAINT uk_platform_job_code UNIQUE (code);