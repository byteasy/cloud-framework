-- 平台资源定义表（平台级）
CREATE TABLE platform_resource (
    -- 主键（自增ID，内部使用）
                               id BIGSERIAL PRIMARY KEY,
    -- 资源编码(需创建唯一索引)
                               code VARCHAR(64) NOT NULL,
    -- 上一级资源编码
                               parent_id BIGINT DEFAULT NULL,
    -- 分组
                               group_id BIGINT DEFAULT NULL,
    -- 资源名称
                               name VARCHAR(1024) NOT NULL,
    -- 资源图标
                               icon VARCHAR(1024) DEFAULT NULL,
    -- 是否显示（0-否，1-显示）
                               visible SMALLINT NOT NULL DEFAULT 1,
    -- 资源类型：0-目录 1-菜单 2-按钮
                               type SMALLINT NOT NULL DEFAULT 1,
    -- 是否打开新的tab：0-否 1-打开
                               open_tab SMALLINT NOT NULL DEFAULT 0,
    -- 在标签模式下是否固定 0：不固定 1：固定
                               affix SMALLINT NOT NULL DEFAULT 0,
    -- 是否开启前端缓存 0：不开启 1：开启
                               keep_alive SMALLINT NOT NULL DEFAULT 0,
    -- 显示顺序，值越小，越靠前
                               show_order SMALLINT NOT NULL DEFAULT 1,
    -- 前端布局
                               layout VARCHAR(128) DEFAULT NULL,
    -- 前端组件路径
                               component VARCHAR(512) DEFAULT NULL,
    -- 前端路由地址
                               router_url VARCHAR(512) DEFAULT NULL,
    -- 重定向地址
                               redirect_url VARCHAR(512) DEFAULT NULL,
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
-- ========== platform_resource 表字段注释 ==========
COMMENT ON COLUMN platform_resource.id IS '主键（自增ID，内部使用）';
COMMENT ON COLUMN platform_resource.code IS '资源编码（业务唯一标识）';
COMMENT ON COLUMN platform_resource.parent_id IS '上一级资源编号';
COMMENT ON COLUMN platform_resource.group_id IS '分组编号';
COMMENT ON COLUMN platform_resource.name IS '资源名称';
COMMENT ON COLUMN platform_resource.icon IS '资源图标';
COMMENT ON COLUMN platform_resource.visible IS '是否显示：0-否，1-显示';
COMMENT ON COLUMN platform_resource.type IS '资源类型：0-目录，1-菜单，2-按钮';
COMMENT ON COLUMN platform_resource.open_tab IS '是否打开新的tab：0-否 1-打开';
COMMENT ON COLUMN platform_resource.affix IS '在标签模式下是否固定：0-不固定，1-固定';
COMMENT ON COLUMN platform_resource.keep_alive IS '是否开启前端缓存：0-不开启，1-开启';
COMMENT ON COLUMN platform_resource.show_order IS '显示顺序，值越小越靠前';
COMMENT ON COLUMN platform_resource.layout IS '前端布局';
COMMENT ON COLUMN platform_resource.component IS '前端组件路径';
COMMENT ON COLUMN platform_resource.router_url IS '前端路由地址';
COMMENT ON COLUMN platform_resource.redirect_url IS '重定向地址';
COMMENT ON COLUMN platform_resource.remark IS '备注';
COMMENT ON COLUMN platform_resource.creator_id IS '创建人账户ID';
COMMENT ON COLUMN platform_resource.created_by IS '创建人姓名';
COMMENT ON COLUMN platform_resource.created_time IS '创建时间';
COMMENT ON COLUMN platform_resource.updater_id IS '更新人账户ID';
COMMENT ON COLUMN platform_resource.updated_by IS '更新人姓名';
COMMENT ON COLUMN platform_resource.updated_time IS '更新时间';
COMMENT ON COLUMN platform_resource.version IS '乐观锁版本号';

-- ========== 表注释 ==========
COMMENT ON TABLE platform_resource IS '平台资源定义表（菜单/目录/按钮权限）';

-- 1. 资源编码唯一索引
CREATE UNIQUE INDEX IF NOT EXISTS uk_platform_resource_code ON platform_resource(code);

ALTER TABLE platform_resource
    ADD CONSTRAINT fk_platform_resource_group_id
        FOREIGN KEY (group_id)
            REFERENCES platform_resource_group(id) ON DELETE CASCADE;

