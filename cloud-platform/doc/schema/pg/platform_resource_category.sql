CREATE TABLE platform_resource_category
(
    id           BIGSERIAL PRIMARY KEY,
    parent_id    BIGINT               DEFAULT NULL,
    code         VARCHAR(64) NOT NULL,
    name         VARCHAR(64) NOT NULL,
    show_order   SMALLINT    NOT NULL DEFAULT 1,
    remark       VARCHAR(512)         DEFAULT NULL,
    creator_id   VARCHAR(64) NOT NULL,
    created_by   VARCHAR(64) NOT NULL,
    created_time TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updater_id   VARCHAR(64)          DEFAULT NULL,
    updated_by   VARCHAR(64)          DEFAULT NULL,
    updated_time TIMESTAMP            DEFAULT NULL,
    version      INTEGER              DEFAULT 1
);
COMMENT ON COLUMN platform_resource_category.id IS '主键（自增ID，内部使用）';
COMMENT ON COLUMN platform_resource_category.parent_id IS '父级编号';
COMMENT ON COLUMN platform_resource_category.code IS '编码';
COMMENT ON COLUMN platform_resource_category.name IS '名称';
COMMENT ON COLUMN platform_resource_category.show_order IS '显示顺序';
COMMENT ON COLUMN platform_resource_category.remark IS '备注';
COMMENT ON COLUMN platform_resource_category.creator_id IS '创建人账户ID';
COMMENT ON COLUMN platform_resource_category.created_by IS '创建人姓名';
COMMENT ON COLUMN platform_resource_category.created_time IS '创建时间';
COMMENT ON COLUMN platform_resource_category.updater_id IS '更新人账户ID';
COMMENT ON COLUMN platform_resource_category.updated_by IS '更新人姓名';
COMMENT ON COLUMN platform_resource_category.updated_time IS '更新时间';
COMMENT ON COLUMN platform_resource_category.version IS '乐观锁版本号';

COMMENT ON TABLE platform_resource_category IS '平台资源分类表';

CREATE UNIQUE INDEX IF NOT EXISTS uk_resource_category_code ON platform_resource_category(code);

