-- 给 platform_schema_define 表新增5个字段
ALTER TABLE platform_schema_define
    ADD COLUMN relation_type SMALLINT DEFAULT NULL,
ADD COLUMN external_class VARCHAR(255) DEFAULT NULL,
ADD COLUMN external_class_primary_field VARCHAR(80) DEFAULT NULL,
ADD COLUMN external_class_display_field VARCHAR(80) DEFAULT NULL,
ADD COLUMN ui_component SMALLINT DEFAULT NULL;
-- 给新增字段添加注释
COMMENT ON COLUMN platform_schema_define.relation_type IS '关联类型';
COMMENT ON COLUMN platform_schema_define.external_class IS '外键关联类';
COMMENT ON COLUMN platform_schema_define.external_class_primary_field IS '外键关联字段';
COMMENT ON COLUMN platform_schema_define.external_class_display_field IS '外键展示字段';
COMMENT ON COLUMN platform_schema_define.ui_component IS '前端组件类型';

-- 平台模型定义
create table platform_entity_define (
    -- 编号
                                        id bigserial primary key,
    -- 编码
                                        code varchar(512) not null,
    -- 服务
                                        service_code varchar(128) not null,
    -- 名称
                                        name varchar(512) not null,
    -- url
                                        url varchar(512) default null,
    -- 说明
                                        remark varchar(2048) default null,
    -- 数据库表名
                                        table_name varchar(128) default null,
    -- 标题属性列
                                        title_column_name varchar(128) default 'name',
    -- 操作列宽度
                                        operation_column_width int default 160,
    -- 默认查询条件
                                        query_params varchar(512) default null,
    -- 表单组件
                                        form_component varchar(220) default null,
    -- Excel导入组件
                                        excel_import_component varchar(220) default null,
    -- 表单组件宽度
                                        form_width varchar(20) default '50%',
    -- 启用对话框
                                        dialog_model  smallint default null,
    -- 启用分页插件
                                        pagination_visible  smallint default null,
    -- 启用Excel导入导出
                                        excel_enable smallint default null,
    -- 启用查询条件
                                        query_form_visible smallint default null,
    -- 启用查询模式
                                        query_mode smallint default null,
    -- 启用合计
                                        show_summary smallint default null,
    -- RowKey
                                        row_key varchar(120) default null,
    -- 默认展开
                                        default_expand_all smallint default null,
    -- 树属性
                                        tree_props varchar(512) default null,
    -- 显示语言
                                        show_lang varchar(60) default null,
    -- 查询栏拓展按钮
                                        query_bar_buttons varchar(512) default null,
    -- 操作列拓展按钮
                                        operation_buttons varchar(512) default null,
    -- 审计字段：创建人编号
                                        creator_id varchar(64) not null,
    -- 审计字段：创建人
                                        created_by varchar(64) not null,
    -- 审计字段：创建时间
                                        created_time timestamp not null default current_timestamp,
    -- 审计字段：更新人编号
                                        updater_id varchar(64) default null,
    -- 审计字段：更新人
                                        updated_by varchar(64) default null,
    -- 审计字段：更新时间
                                        updated_time timestamp default null,
    -- 乐观锁
                                        version integer default 1
);

-- 补全 platform_entity_define 所有字段注释
comment on column platform_entity_define.id is '编号';
comment on column platform_entity_define.code is '编码';
comment on column platform_entity_define.service_code is '服务编码';
comment on column platform_entity_define.name is '名称';
comment on column platform_entity_define.url is '请求接口地址';
comment on column platform_entity_define.remark is '说明';
comment on column platform_entity_define.title_column_name is '卡片模式标题字段名称';
comment on column platform_entity_define.operation_column_width is '操作列宽度';
comment on column platform_entity_define.query_params is '默认查询条件';
comment on column platform_entity_define.form_component is '自定义表单组件';
comment on column platform_entity_define.form_width is '弹窗宽度';
comment on column platform_entity_define.dialog_model is '弹窗方式';
comment on column platform_entity_define.pagination_visible is '启用分页插件';
comment on column platform_entity_define.excel_enable is '是否启用excel导入导出';
comment on column platform_entity_define.query_form_visible is '开启查询条件栏';
comment on column platform_entity_define.excel_import_component is '自定义导入excel组件';
comment on column platform_entity_define.row_key is '树形表格rowkey';
comment on column platform_entity_define.default_expand_all is '默认展开树';
comment on column platform_entity_define.tree_props is '树属性名称';
comment on column platform_entity_define.query_mode is '查询模式';
comment on column platform_entity_define.show_summary is '显示表尾合计';
comment on column platform_entity_define.show_lang is '显示语言';
comment on column platform_entity_define.creator_id is '创建人编号';
comment on column platform_entity_define.created_by is '创建人';
comment on column platform_entity_define.created_time is '创建时间';
comment on column platform_entity_define.updater_id is '更新人编号';
comment on column platform_entity_define.updated_by is '更新人';
comment on column platform_entity_define.updated_time is '更新时间';
comment on column platform_entity_define.version is '乐观锁版本';

comment on table platform_entity_define is '平台模型定义';


alter table platform_entity_define
    add constraint fk_platform_entity_define_service_code
        foreign key (service_code)
            references platform_service_instance(code) on delete cascade;

ALTER TABLE "platform_schema_define"
    ADD COLUMN IF NOT EXISTS "field_name" VARCHAR(255) DEFAULT NULL,
    ADD COLUMN IF NOT EXISTS "description" VARCHAR(1024) DEFAULT NULL,
    ADD COLUMN IF NOT EXISTS "encrypt" SMALLINT NOT NULL DEFAULT 1,
    ADD COLUMN IF NOT EXISTS "desensitize" SMALLINT NOT NULL DEFAULT 1;

COMMENT ON COLUMN "platform_schema_define"."field_name" IS '字段名';
COMMENT ON COLUMN "platform_schema_define"."description" IS '说明';
COMMENT ON COLUMN "platform_schema_define"."encrypt" IS '是否加密';
COMMENT ON COLUMN "platform_schema_define"."desensitize" IS '是否脱敏';

ALTER TABLE "platform_entity_define"
    ADD COLUMN IF NOT EXISTS "field_component" VARCHAR(512) DEFAULT NULL;
COMMENT ON COLUMN "platform_entity_define"."field_component" IS '关联对象前端组件';

ALTER TABLE "platform_entity_define"
    ADD COLUMN IF NOT EXISTS "query_bar_buttons" VARCHAR(512) DEFAULT NULL;
ALTER TABLE "platform_entity_define"
    ADD COLUMN IF NOT EXISTS "operation_buttons" VARCHAR(512) DEFAULT NULL;
COMMENT ON COLUMN "platform_entity_define"."query_bar_buttons" IS '查询栏拓展按钮';
COMMENT ON COLUMN "platform_entity_define"."operation_buttons" IS '操作列拓展按钮';

ALTER TABLE "platform_schema_define"
    ADD COLUMN IF NOT EXISTS "inner_primary_field" VARCHAR(80) DEFAULT NULL;

COMMENT ON COLUMN "platform_schema_define"."inner_primary_field" IS '明细表关联字段';

ALTER TABLE "platform_entity_define"
    ADD COLUMN IF NOT EXISTS "table_name" VARCHAR(128) DEFAULT NULL;
COMMENT ON COLUMN "platform_entity_define"."table_name" IS '数据库表名';


-- 2026-06-05

ALTER TABLE "platform_schema_define"
    ADD COLUMN IF NOT EXISTS "column_component" VARCHAR(512) DEFAULT NULL;

COMMENT ON COLUMN "platform_schema_define"."column_component" IS '表格列自定义组件';