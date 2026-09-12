CREATE TABLE platform_js_script
(
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(512) NOT NULL,
    script       TEXT DEFAULT NULL,
    creator_id   VARCHAR(64)  NOT NULL,
    created_by   VARCHAR(64)  NOT NULL,
    created_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updater_id   VARCHAR(64)           DEFAULT NULL,
    updated_by   VARCHAR(64)           DEFAULT NULL,
    updated_time TIMESTAMP             DEFAULT NULL,
    version      INTEGER               DEFAULT 1
);

COMMENT ON COLUMN platform_js_script.id IS '主键';
COMMENT ON COLUMN platform_js_script.name IS '名称';
COMMENT ON COLUMN platform_js_script.script IS '脚本';
COMMENT ON COLUMN platform_js_script.creator_id IS '创建人账户ID';
COMMENT ON COLUMN platform_js_script.created_by IS '创建人姓名';
COMMENT ON COLUMN platform_js_script.created_time IS '创建时间';
COMMENT ON COLUMN platform_js_script.updater_id IS '更新人账户ID';
COMMENT ON COLUMN platform_js_script.updated_by IS '更新人姓名';
COMMENT ON COLUMN platform_js_script.updated_time IS '更新时间';
COMMENT ON COLUMN platform_js_script.version IS '乐观锁版本号';

COMMENT ON TABLE platform_js_script IS 'JS脚本';