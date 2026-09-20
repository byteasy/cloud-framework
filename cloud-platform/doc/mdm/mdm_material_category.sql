create table platform_entity_define
(
    "id"                    int8        NOT NULL,
    "classification_code"   varchar(64) COLLATE "pg_catalog"."default",
    "parent_id"             int8,
    "ancestors"             varchar(2000) COLLATE "pg_catalog"."default",
    "classification_name"   varchar(100) COLLATE "pg_catalog"."default",
    "is_leaf"               varchar(50) COLLATE "pg_catalog"."default",
    "classification_order"  int4,
    "classification_status" varchar(50) COLLATE "pg_catalog"."default",
    "is_deleted"            int4                                       DEFAULT 0,
    "remark"                varchar(255) COLLATE "pg_catalog"."default",
    "status"                int4                                       DEFAULT 1,
    "tenant_id"             varchar(12) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
    "tier"                  varchar(50) COLLATE "pg_catalog"."default",
    "extend_field"          jsonb,
    creator_id              varchar(64) not null,
    created_by              varchar(64) not null,
    created_time            timestamp   not null                       default current_timestamp,
    updater_id              varchar(64)                                default null,
    updated_by              varchar(64)                                default null,
    updated_time            timestamp                                  default null,
    version                 integer                                    default 1
);

comment on column platform_entity_define.id is '编号';
comment on column platform_entity_define.code is '编码';
comment on column platform_entity_define.table_name is '数据库表名';
comment on column platform_entity_define.service_code is '服务编码';
comment on column platform_entity_define.resource_id is '资源编号';
comment on column platform_entity_define.std_layout_params_id is '布局参数';
comment on column platform_entity_define.name is '名称';
comment on column platform_entity_define.url is '请求接口地址';
comment on column platform_entity_define.remark is '说明';
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

alter table platform_entity_define
    add constraint fk_platform_entity_define_resource_id
        foreign key (resource_id)
            references platform_resource(id) on delete cascade;

ALTER TABLE "platform_entity_define"
    ADD CONSTRAINT uk_platform_entity_define_code UNIQUE (code);