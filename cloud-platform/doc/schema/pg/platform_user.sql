-- 通用用户表（平台级）
CREATE TABLE platform_user (
    -- 主键（自增ID，内部使用）
                               id BIGSERIAL PRIMARY KEY,
    -- 用户ID
                               user_id UUID NOT NULL DEFAULT gen_random_uuid(),
    -- 登录账号
                               username VARCHAR(64) NOT NULL,
    -- 密码
                               password VARCHAR(256) NOT NULL,
    -- 密码盐值
                               salt VARCHAR(64) DEFAULT '',
    -- 账号过期时间
                               account_expired_at TIMESTAMP DEFAULT NULL,
    -- 密码过期时间
                               credentials_expired_at TIMESTAMP DEFAULT NULL,
    -- 真实姓名
                               real_name VARCHAR(64) DEFAULT '',
    -- 昵称
                               nickname VARCHAR(64) DEFAULT '',
    -- 性别
                               gender SMALLINT DEFAULT 0,
    -- 出生日期
                               birthday DATE DEFAULT NULL,
    -- 头像URL
                               avatar VARCHAR(512) DEFAULT '',
    -- 手机号
                               mobile VARCHAR(20) DEFAULT '',
    -- 手机号是否验证
                               mobile_verified SMALLINT NOT NULL DEFAULT 1,
    -- 邮箱
                               email VARCHAR(128) DEFAULT '',
    -- 邮箱是否验证
                               email_verified SMALLINT NOT NULL DEFAULT 1,
    -- 用户状态
                               status SMALLINT NOT NULL DEFAULT 1,
    -- 用户类型
                               user_type SMALLINT NOT NULL DEFAULT 0,
    -- 登录失败次数
                               login_fail_count SMALLINT NOT NULL DEFAULT 0,
    -- 账号锁定时间
                               lock_time TIMESTAMP DEFAULT NULL,
    -- 最后登录时间
                               last_login_time TIMESTAMP DEFAULT NULL,
    -- 最后登录IP
                               last_login_ip VARCHAR(64) DEFAULT '',
    -- 多租户ID（支撑SaaS系统）
                               tenant_id VARCHAR(64) DEFAULT 'default',
    -- 领域编码
                               realm_id BIGINT DEFAULT NULL,
    -- 所属组织编号
                               org_id BIGINT DEFAULT NULL,
    -- 所属部门编号
                               dept_id BIGINT DEFAULT NULL,
    -- 自定义扩展属性
                               ext_info JSONB DEFAULT '{}'::jsonb,
    -- 备注
                               remark VARCHAR(512) DEFAULT NULL,
    -- 审计字段：创建人账户ID
                               creator_id VARCHAR(64) DEFAULT NULL,
    -- 审计字段：创建人（用户ID）
                               created_by VARCHAR(64) DEFAULT NULL,
    -- 审计字段：创建时间
                               created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- 审计字段：更新人账户ID
                               updater_id VARCHAR(64) DEFAULT NULL,
    -- 审计字段：更新人姓名
                               updated_by VARCHAR(64) DEFAULT NULL,
    -- 审计字段：更新时间
                               updated_time TIMESTAMP DEFAULT NULL,
    -- 逻辑删除标识（0-未删除，1-已删除）
                               is_deleted SMALLINT NOT NULL DEFAULT 0,
    -- 审计字段：删除人账户ID
                               deleter_id VARCHAR(64) DEFAULT '',
    -- 审计字段：删除人姓名
                               deleted_by VARCHAR(64) DEFAULT '',
    -- 审计字段：删除时间
                               deleted_time TIMESTAMP DEFAULT NULL,
    -- 乐观锁
                               version INTEGER DEFAULT 1,
    -- 仅保留user_id的普通唯一约束（UUID天然唯一，不受删除/注销状态影响）
                               CONSTRAINT uk_platform_user_user_id UNIQUE (user_id)
);

-- ========== 补充PostgreSQL专属：字段注释（核心修复） ==========
COMMENT ON COLUMN platform_user.id IS '主键（自增ID，内部使用）';
COMMENT ON COLUMN platform_user.user_id IS '业务唯一标识（UUID，对外暴露/集成用，避免自增ID泄露）';
COMMENT ON COLUMN platform_user.username IS '登录账号（支持手机号/邮箱/自定义账号，租户内唯一）';
COMMENT ON COLUMN platform_user.password IS '密码（加密存储）';
COMMENT ON COLUMN platform_user.salt IS '密码盐值';
COMMENT ON COLUMN platform_user.real_name IS '真实姓名';
COMMENT ON COLUMN platform_user.nickname IS '用户昵称';
COMMENT ON COLUMN platform_user.gender IS '性别：0-未知，1-男，2-女';
COMMENT ON COLUMN platform_user.birthday IS '出生日期';
COMMENT ON COLUMN platform_user.avatar IS '头像URL';
COMMENT ON COLUMN platform_user.mobile IS '手机号';
COMMENT ON COLUMN platform_user.mobile_verified IS '手机号是否验证';
COMMENT ON COLUMN platform_user.email IS '邮箱';
COMMENT ON COLUMN platform_user.email_verified IS '邮箱是否验证';
COMMENT ON COLUMN platform_user.status IS '用户状态：0-禁用，1-正常，2-锁定，3-注销';
COMMENT ON COLUMN platform_user.user_type IS '用户类型：0-普通用户，1-管理员，2-系统用户，9-第三方用户';
COMMENT ON COLUMN platform_user.login_fail_count IS '登录失败次数（防暴力破解）';
COMMENT ON COLUMN platform_user.lock_time IS '账号锁定时间';
COMMENT ON COLUMN platform_user.last_login_time IS '最后登录时间';
COMMENT ON COLUMN platform_user.last_login_ip IS '最后登录IP';
COMMENT ON COLUMN platform_user.tenant_id IS '租户ID（多租户隔离）';
COMMENT ON COLUMN platform_user.org_code IS '所属组织编码';
COMMENT ON COLUMN platform_user.dept_code IS '所属部门编码';
COMMENT ON COLUMN platform_user.ext_info IS '扩展属性（JSONB格式）';
COMMENT ON COLUMN platform_user.remark IS '备注';
COMMENT ON COLUMN platform_user."creator_id" IS '创建人账号ID';
COMMENT ON COLUMN platform_user."created_by" IS '创建人姓名';
COMMENT ON COLUMN platform_user."created_time" IS '创建时间';
COMMENT ON COLUMN platform_user."updater_id" IS '更新人账号ID';
COMMENT ON COLUMN platform_user."updated_by" IS '更新人姓名';
COMMENT ON COLUMN platform_user."updated_time" IS '更新时间';
COMMENT ON COLUMN platform_user."deleter_id" IS '删除人账号ID';
COMMENT ON COLUMN platform_user."deleted_by" IS '删除人姓名';
COMMENT ON COLUMN platform_user."deleted_time" IS '删除时间';
COMMENT ON COLUMN platform_user."version" IS '乐观锁';

-- ========== 表注释 ==========
COMMENT ON TABLE platform_user IS '通用平台用户表';

-- ========== 核心：精准的部分唯一索引（约束未删除+非注销的用户） ==========
CREATE UNIQUE INDEX uk_platform_user_username_tenant ON platform_user (username, tenant_id)
    WHERE is_deleted = 0 AND status != 3;

CREATE UNIQUE INDEX uk_platform_user_mobile_tenant ON platform_user (mobile, tenant_id)
    WHERE is_deleted = 0 AND status != 3 AND mobile <> '';

CREATE UNIQUE INDEX uk_platform_user_email_tenant ON platform_user (email, tenant_id)
    WHERE is_deleted = 0 AND status != 3 AND email <> '';

-- ========== 性能索引 ==========
CREATE INDEX idx_platform_user_tenant_id ON platform_user(tenant_id);
CREATE INDEX idx_platform_user_org_id ON platform_user(org_id);
CREATE INDEX idx_platform_user_dept_id ON platform_user(dept_id);
CREATE INDEX idx_platform_user_status ON platform_user(status);
CREATE INDEX idx_platform_user_user_type ON platform_user(user_type);
CREATE INDEX idx_platform_user_created_time ON platform_user(created_time);
CREATE INDEX idx_platform_user_last_login_time ON platform_user(last_login_time);
CREATE INDEX idx_platform_user_is_deleted_status ON platform_user(is_deleted, status);
