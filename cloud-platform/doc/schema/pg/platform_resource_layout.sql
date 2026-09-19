CREATE TABLE platform_resource_layout
(
    id                   BIGSERIAL PRIMARY KEY,
    resource_id          BIGINT        NOT NULL,
    name                 VARCHAR(1024) NOT NULL,
    is_default           BOOL                   DEFAULT FALSE,
    is_public            BOOL                   DEFAULT FALSE,
    type                 SMALLINT      NOT NULL,
    std_layout_params_id BIGINT                 DEFAULT NULL,
    component_path       VARCHAR(128)           DEFAULT NULL,
    component_param      VARCHAR(128)           DEFAULT NULL,
    creator_id           VARCHAR(64)   NOT NULL,
    created_by           VARCHAR(64)   NOT NULL,
    created_time         TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updater_id           VARCHAR(64)            DEFAULT NULL,
    updated_by           VARCHAR(64)            DEFAULT NULL,
    updated_time         TIMESTAMP              DEFAULT NULL,
    version              INTEGER                DEFAULT 1
);

COMMENT ON COLUMN platform_resource_layout.id IS '主键';
COMMENT ON COLUMN platform_resource_layout.resource_id IS '菜单';
-- 0： 标准布局 1：EB布局 2: 定制开发布局
COMMENT ON COLUMN platform_resource_layout.name IS '名称';
COMMENT ON COLUMN platform_resource_layout.is_default IS '默认布局';
COMMENT ON COLUMN platform_resource_layout.is_public IS '公开访问';
COMMENT ON COLUMN platform_resource_layout.type IS '类型';
COMMENT ON COLUMN platform_resource_layout.component_path IS '组件路径';
COMMENT ON COLUMN platform_resource_layout.component_param IS '组件参数';
COMMENT ON COLUMN platform_resource_layout.creator_id IS '创建人编号';
COMMENT ON COLUMN platform_resource_layout.created_by IS '创建人姓名';
COMMENT ON COLUMN platform_resource_layout.created_time IS '创建时间';
COMMENT ON COLUMN platform_resource_layout.updater_id IS '更新人编号';
COMMENT ON COLUMN platform_resource_layout.updated_by IS '更新人姓名';
COMMENT ON COLUMN platform_resource_layout.updated_time IS '更新时间';
COMMENT ON COLUMN platform_resource_layout.version IS '版本号';

COMMENT ON TABLE platform_resource_layout IS '页面布局';

alter table platform_resource_layout
    add constraint fk_platform_resource_layout_resource_id
        foreign key (resource_id)
            references platform_resource(id) on delete cascade;