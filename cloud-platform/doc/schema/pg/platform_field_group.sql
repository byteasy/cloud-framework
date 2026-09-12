CREATE TABLE "platform_field_group"
(
    id                           BIGSERIAL PRIMARY KEY,
    class_name                   VARCHAR(255) NOT NULL,
    name                         VARCHAR(255) NOT NULL,
    creator_id                   VARCHAR(64)  NOT NULL,
    created_by                   VARCHAR(64)  NOT NULL,
    created_time                 TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updater_id                   VARCHAR(64)           DEFAULT NULL,
    updated_by                   VARCHAR(64)           DEFAULT NULL,
    updated_time                 TIMESTAMP             DEFAULT NULL,
    version                      INTEGER               DEFAULT 1
);

COMMENT ON TABLE "platform_field_group" IS '字段分组';
COMMENT ON COLUMN "platform_field_group"."id" IS '编号';
COMMENT ON COLUMN "platform_field_group"."class_name" IS '类';
COMMENT ON COLUMN "platform_field_group"."name" IS '名称';

COMMENT ON COLUMN "platform_field_group".created_by IS '创建人ID';
COMMENT ON COLUMN "platform_field_group".created_time IS '创建时间';
COMMENT ON COLUMN "platform_field_group".updated_by IS '更新人ID';
COMMENT ON COLUMN "platform_field_group".updated_time IS '更新时间';
COMMENT ON COLUMN "platform_field_group".version IS '乐观锁版本号';

CREATE INDEX idx_platform_field_group_class_name ON "platform_field_group" ("class_name");

ALTER TABLE platform_field_group
    ADD CONSTRAINT fk_platform_field_group_class_name
        FOREIGN KEY (class_name)
            REFERENCES platform_entity_define(code) ON DELETE CASCADE;