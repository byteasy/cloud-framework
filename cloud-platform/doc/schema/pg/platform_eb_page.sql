CREATE TABLE platform_eb_page
(
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(512) NOT NULL,
    uri             VARCHAR(256)          DEFAULT NULL,
    status          SMALLINT     NOT NULL DEFAULT 1,
    creator_id      VARCHAR(64)  NOT NULL,
    created_by      VARCHAR(64)  NOT NULL,
    created_time    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updater_id      VARCHAR(64)           DEFAULT NULL,
    updated_by      VARCHAR(64)           DEFAULT NULL,
    updated_time    TIMESTAMP             DEFAULT NULL,
    version         INTEGER               DEFAULT 1
);

COMMENT ON COLUMN platform_eb_page.id IS '编号';
COMMENT ON COLUMN platform_eb_page.name IS '名称';
COMMENT ON COLUMN platform_eb_page.uri IS 'URI';
COMMENT ON COLUMN platform_eb_page.status IS '状态';
COMMENT ON COLUMN platform_eb_page.creator_id IS '创建人编号';
COMMENT ON COLUMN platform_eb_page.created_by IS '创建人';
COMMENT ON COLUMN platform_eb_page.created_time IS '创建时间';
COMMENT ON COLUMN platform_eb_page.updater_id IS '更新人编号';
COMMENT ON COLUMN platform_eb_page.updated_by IS '更新人';
COMMENT ON COLUMN platform_eb_page.updated_time IS '更新时间';
COMMENT ON COLUMN platform_eb_page.version IS '版本';

COMMENT ON TABLE platform_eb_page IS '低代码页面';

