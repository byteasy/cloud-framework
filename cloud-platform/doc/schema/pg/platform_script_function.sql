CREATE TABLE platform_script_function
(
    id           BIGSERIAL PRIMARY KEY,
    service_code VARCHAR(255) DEFAULT NULL,
    group_name   VARCHAR(64)  NOT NULL,
    name         VARCHAR(512) NOT NULL,
    description  VARCHAR(2048)         DEFAULT NULL,
    creator_id   VARCHAR(64)  NOT NULL,
    created_by   VARCHAR(64)  NOT NULL,
    created_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updater_id   VARCHAR(64)           DEFAULT NULL,
    updated_by   VARCHAR(64)           DEFAULT NULL,
    updated_time TIMESTAMP             DEFAULT NULL,
    version      INTEGER               DEFAULT 1
);

COMMENT ON COLUMN platform_script_function.id IS '编号';
COMMENT ON COLUMN platform_script_function.service_code IS '服务编码';
COMMENT ON COLUMN platform_script_function.group_name IS '分组';
COMMENT ON COLUMN platform_script_function.name IS '名称';
COMMENT ON COLUMN platform_script_function.description IS '说明';
COMMENT ON COLUMN platform_script_function.creator_id IS '创建人编号';
COMMENT ON COLUMN platform_script_function.created_by IS '创建人';
COMMENT ON COLUMN platform_script_function.created_time IS '创建时间';
COMMENT ON COLUMN platform_script_function.updater_id IS '更新人编号';
COMMENT ON COLUMN platform_script_function.updated_by IS '更新人';
COMMENT ON COLUMN platform_script_function.updated_time IS '更新时间';
COMMENT ON COLUMN platform_script_function.version IS '版本';

COMMENT ON TABLE platform_script_function IS '脚本函数定义';

ALTER TABLE platform_script_function
    ADD CONSTRAINT fk_platform_script_function_service_code
        FOREIGN KEY (service_code)
            REFERENCES platform_service_instance(code) ON DELETE CASCADE;

ALTER TABLE platform_script_function
    ADD CONSTRAINT uk_platform_script_function_name UNIQUE (name);