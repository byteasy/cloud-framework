CREATE TABLE platform_openapi
(
    id              BIGSERIAL PRIMARY KEY,
    code            VARCHAR(255) NOT NULL,
    service_code    VARCHAR(255) NOT NULL,
    name            VARCHAR(512) NOT NULL,
    type            SMALLINT     NOT NULL DEFAULT 1,
    class_name      VARCHAR(255) NOT NULL,
    http_method     VARCHAR(32)  NOT NULL,
    uri             VARCHAR(256)          DEFAULT NULL,
    permission_type SMALLINT     NOT NULL DEFAULT 2,
    loggable        BOOL                  DEFAULT FALSE,
    pre_script_id   BIGINT                DEFAULT NULL,
    script_id       BIGINT                DEFAULT NULL,
    post_script_id  BIGINT                DEFAULT NULL,
    remark          varchar(2048)         DEFAULT NULL,
    creator_id      VARCHAR(64)  NOT NULL,
    created_by      VARCHAR(64)  NOT NULL,
    created_time    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updater_id      VARCHAR(64)           DEFAULT NULL,
    updated_by      VARCHAR(64)           DEFAULT NULL,
    updated_time    TIMESTAMP             DEFAULT NULL,
    version         INTEGER               DEFAULT 1
);

COMMENT ON COLUMN platform_openapi.id IS '编号';
COMMENT ON COLUMN platform_openapi.code IS '编码';
COMMENT ON COLUMN platform_openapi.service_code IS '服务编码';
COMMENT ON COLUMN platform_openapi.name IS '接口名称';
COMMENT ON COLUMN platform_openapi.type IS '接口类型';
COMMENT ON COLUMN platform_openapi.class_name IS '模型';
COMMENT ON COLUMN platform_openapi.http_method IS 'Http方法';
COMMENT ON COLUMN platform_openapi.uri IS 'URI';
COMMENT ON COLUMN platform_openapi.permission_type IS '权限控制类型 0：开放访问 1：登录用户即可访问 2：授权后才可访问';
COMMENT ON COLUMN platform_openapi.loggable IS '记录日志';
COMMENT ON COLUMN platform_openapi.pre_script_id IS '前置脚本钩子';
COMMENT ON COLUMN platform_openapi.script_id IS '函数脚本';
COMMENT ON COLUMN platform_openapi.post_script_id IS '后置脚本钩子';
COMMENT ON COLUMN platform_openapi.remark IS '说明';
COMMENT ON COLUMN platform_openapi.creator_id IS '创建人编号';
COMMENT ON COLUMN platform_openapi.created_by IS '创建人';
COMMENT ON COLUMN platform_openapi.created_time IS '创建时间';
COMMENT ON COLUMN platform_openapi.updater_id IS '更新人编号';
COMMENT ON COLUMN platform_openapi.updated_by IS '更新人';
COMMENT ON COLUMN platform_openapi.updated_time IS '更新时间';
COMMENT ON COLUMN platform_openapi.version IS '版本';

COMMENT ON TABLE platform_openapi IS '平台接口定义';

ALTER TABLE platform_openapi
    ADD CONSTRAINT fk_platform_openapi_js_script_id
        FOREIGN KEY (script_id)
            REFERENCES platform_js_script(id) ON DELETE CASCADE;

ALTER TABLE platform_openapi
    ADD CONSTRAINT uk_platform_openapi_code UNIQUE (code);

alter table platform_openapi
    add constraint fk_platform_openapi_service_code
        foreign key (service_code)
            references platform_service_instance(code) on delete cascade;

alter table platform_openapi
    add constraint fk_platform_openapi_class_name
        foreign key (class_name)
            references platform_entity_define(code) on delete cascade;

