CREATE TABLE platform_service_instance
(
    id           BIGSERIAL PRIMARY KEY,
    code         VARCHAR(64)  NOT NULL,
    context_path VARCHAR(512) NOT NULL,
    status       SMALLINT     NOT NULL DEFAULT 1,
    name         VARCHAR(128) NOT NULL,
    remark       VARCHAR(512)          DEFAULT NULL,
    category_id  BIGINT       DEFAULT NULL,
    creator_id   VARCHAR(64)  NOT NULL,
    created_by   VARCHAR(64)  NOT NULL,
    created_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updater_id   VARCHAR(64)           DEFAULT NULL,
    updated_by   VARCHAR(64)           DEFAULT NULL,
    updated_time TIMESTAMP             DEFAULT NULL,
    version      INTEGER               DEFAULT 1
);

-- ========== platform_service_instance 表字段注释 ==========
COMMENT ON COLUMN platform_service_instance.id IS '编号';
COMMENT ON COLUMN platform_service_instance.code IS '编码';
COMMENT ON COLUMN platform_service_instance.context_path IS '路径';
COMMENT ON COLUMN platform_service_instance.status IS '状态';
COMMENT ON COLUMN platform_service_instance.name IS '名称';
COMMENT ON COLUMN platform_service_instance.remark IS '备注';
COMMENT ON COLUMN platform_service_instance.category_id IS '分类编号';
COMMENT ON COLUMN platform_service_instance.creator_id IS '创建人编号';
COMMENT ON COLUMN platform_service_instance.created_by IS '创建人';
COMMENT ON COLUMN platform_service_instance.created_time IS '创建时间';
COMMENT ON COLUMN platform_service_instance.updater_id IS '更新人编号';
COMMENT ON COLUMN platform_service_instance.updated_by IS '更新人';
COMMENT ON COLUMN platform_service_instance.updated_time IS '更新时间';
COMMENT ON COLUMN platform_service_instance.version IS '版本号';

COMMENT ON TABLE platform_service_instance IS '平台服务定义表';

ALTER TABLE platform_service_instance
    ADD CONSTRAINT uk_platform_service_instance_code UNIQUE (code);