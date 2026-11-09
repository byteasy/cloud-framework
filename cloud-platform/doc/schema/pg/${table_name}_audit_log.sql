create table ${table_name}_audit_log
(
    id             bigserial primary key,
    entity_id      varchar(64)      NOT NULL,
    change_type    SMALLINT    NOT NULL DEFAULT 0,
    entity_content TEXT        not null,

    creator_id     varchar(64) not null,
    created_by     varchar(64) not null,
    created_time   timestamp   not null default current_timestamp
);

comment on column ${table_name}_audit_log.id is '编号';
comment on column ${table_name}_audit_log.entity_id is '实体编码';
comment on column ${table_name}_audit_log.change_type is '变更类型：0：新增 1：编辑 2：删除';
comment on column ${table_name}_audit_log.entity_content is '变更内容';
comment on column ${table_name}_audit_log.creator_id is '创建人编号';
comment on column ${table_name}_audit_log.created_by is '创建人';
comment on column ${table_name}_audit_log.created_time is '创建时间';

comment on table ${table_name}_audit_log is '审计日志定义表';