-- 登录日志
CREATE TABLE platform_login_log (
    -- 主键
                               id BIGSERIAL PRIMARY KEY,
    -- 类型 0: 登入 1：登出
                               type SMALLINT NOT NULL DEFAULT 0,
    -- 账号
                               username VARCHAR(64) NOT NULL,
    -- 姓名
                               real_name VARCHAR(64) DEFAULT '',
    -- IP
                               ip_address VARCHAR(64) DEFAULT '',
    -- session
                               session_id VARCHAR(64) DEFAULT '',
    -- 备注
                               remark VARCHAR(512) DEFAULT NULL,
    -- 审计字段：创建人账户编号
                               creator_id VARCHAR(64) NOT NULL,
    -- 审计字段：创建人
                               created_by VARCHAR(64) NOT NULL,
    -- 审计字段：创建时间
                               created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ========== platform_service 表字段注释 ==========
COMMENT ON COLUMN platform_login_log.id IS '主键';
COMMENT ON COLUMN platform_login_log.type IS '类型';
COMMENT ON COLUMN platform_login_log.username IS '账号';
COMMENT ON COLUMN platform_login_log.real_name IS '姓名';
COMMENT ON COLUMN platform_login_log.ip_address IS 'IP';
COMMENT ON COLUMN platform_login_log.session_id IS 'session';
COMMENT ON COLUMN platform_login_log.remark IS '备注';
COMMENT ON COLUMN platform_login_log.creator_id IS '创建人账户编号';
COMMENT ON COLUMN platform_login_log.created_by IS '创建人姓名';
COMMENT ON COLUMN platform_login_log.created_time IS '创建时间';

-- ========== 表注释 ==========
COMMENT ON TABLE platform_login_log IS '登录日志';