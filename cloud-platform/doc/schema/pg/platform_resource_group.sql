CREATE TABLE platform_resource_group
(
    id           BIGSERIAL PRIMARY KEY,
    category_id  BIGINT       NOT NULL,
    code         VARCHAR(128)  NOT NULL,
    service_code VARCHAR(128)  NOT NULL,
    name         VARCHAR(1024)  NOT NULL,
    show_order   SMALLINT     NOT NULL DEFAULT 1,
    navbar_menu  SMALLINT     NOT NULL DEFAULT 0,
    is_microapp  SMALLINT     NOT NULL DEFAULT 0,
    entry        VARCHAR(512)          DEFAULT NULL,
    props        VARCHAR(512)          DEFAULT NULL,
    remark       VARCHAR(512)          DEFAULT NULL,
    creator_id   VARCHAR(64)  NOT NULL,
    created_by   VARCHAR(64)  NOT NULL,
    created_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updater_id   VARCHAR(64)           DEFAULT NULL,
    updated_by   VARCHAR(64)           DEFAULT NULL,
    updated_time TIMESTAMP             DEFAULT NULL,
    version      INTEGER               DEFAULT 1
);
COMMENT ON COLUMN platform_resource_group.id IS '主键（自增ID，内部使用）';
COMMENT ON COLUMN platform_resource_group.category_id IS '资源分类';
COMMENT ON COLUMN platform_resource_group.code IS '编码';
COMMENT ON COLUMN platform_resource_group.service_code IS '服务编码';
COMMENT ON COLUMN platform_resource_group.name IS '名称';
COMMENT ON COLUMN platform_resource_group.show_order IS '显示顺序';
COMMENT ON COLUMN platform_resource_group.navbar_menu IS '快捷菜单';
COMMENT ON COLUMN platform_resource_group.is_microapp IS '是否微应用';
COMMENT ON COLUMN platform_resource_group.entry IS '入口地址';
COMMENT ON COLUMN platform_resource_group.props IS '拓展属性';
COMMENT ON COLUMN platform_resource_group.remark IS '备注';
COMMENT ON COLUMN platform_resource_group.creator_id IS '创建人账户ID';
COMMENT ON COLUMN platform_resource_group.created_by IS '创建人姓名';
COMMENT ON COLUMN platform_resource_group.created_time IS '创建时间';
COMMENT ON COLUMN platform_resource_group.updater_id IS '更新人账户ID';
COMMENT ON COLUMN platform_resource_group.updated_by IS '更新人姓名';
COMMENT ON COLUMN platform_resource_group.updated_time IS '更新时间';
COMMENT ON COLUMN platform_resource_group.version IS '乐观锁版本号';

COMMENT ON TABLE platform_resource_group IS '菜单分组';

CREATE UNIQUE INDEX IF NOT EXISTS uk_resource_group_code ON platform_resource_group(code);

ALTER TABLE platform_resource_group
    ADD CONSTRAINT fk_platform_resource_group_service_code
        FOREIGN KEY (service_code)
            REFERENCES platform_service_instance(code) ON DELETE CASCADE;
