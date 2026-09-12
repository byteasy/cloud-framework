-- 平台国际化翻译语言包
CREATE TABLE platform_group_i18n (
    -- 主键
                               id BIGSERIAL PRIMARY KEY,
    -- 应用编号
                               group_id BIGINT NOT NULL,
    -- 编码
                               code VARCHAR(64) NOT NULL,
    -- 文案
                               name VARCHAR(1024) DEFAULT NULL,
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
COMMENT ON COLUMN platform_group_i18n.id IS '主键';
COMMENT ON COLUMN platform_group_i18n.group_id IS '应用编号';
COMMENT ON COLUMN platform_group_i18n.code IS '编码';
COMMENT ON COLUMN platform_group_i18n.name IS '文案';
COMMENT ON COLUMN platform_group_i18n.creator_id IS '创建人账户ID';
COMMENT ON COLUMN platform_group_i18n.created_by IS '创建人姓名';
COMMENT ON COLUMN platform_group_i18n.created_time IS '创建时间';
COMMENT ON COLUMN platform_group_i18n.updater_id IS '更新人账户ID';
COMMENT ON COLUMN platform_group_i18n.updated_by IS '更新人姓名';
COMMENT ON COLUMN platform_group_i18n.updated_time IS '更新时间';
COMMENT ON COLUMN platform_group_i18n.version IS '乐观锁版本号';

-- ========== 表注释 ==========
COMMENT ON TABLE platform_group_i18n IS '平台国际化翻译语言包';
CREATE UNIQUE INDEX idx_platform_group_i18n_group_id_code
    ON platform_group_i18n (group_id, code);