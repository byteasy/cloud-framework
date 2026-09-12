CREATE TABLE "platform_field_define"
(
    id                           BIGSERIAL PRIMARY KEY,
    class_name                   VARCHAR(255),
    name                         VARCHAR(255) NOT NULL,
    field_name                   VARCHAR(255)          DEFAULT NULL,
    description                  VARCHAR(1024)         DEFAULT NULL,
    cn_name                      VARCHAR(255),
    i18n_key                     VARCHAR(100),
    display_name                 VARCHAR(1024),
    type                         SMALLINT     NOT NULL DEFAULT 0,
    type_class_name              VARCHAR(255),
    group_id                     BIGINT       DEFAULT NULL,
    is_primary                   SMALLINT     NOT NULL DEFAULT 0,
    is_show                      SMALLINT     NOT NULL DEFAULT 1,
    is_sort                      SMALLINT     NOT NULL DEFAULT 0,
    is_overflow                  SMALLINT     NOT NULL DEFAULT 0,
    is_i18n                      SMALLINT     NOT NULL DEFAULT 0,
    is_search                    SMALLINT     NOT NULL DEFAULT 1,
    is_fixed                     SMALLINT     NOT NULL DEFAULT 0,
    is_require                   SMALLINT     NOT NULL DEFAULT 0,
    is_form                      SMALLINT     NOT NULL DEFAULT 1,
    textarea_rows                SMALLINT     NOT NULL DEFAULT 1,
    encrypt                      SMALLINT     NOT NULL DEFAULT 0,
    desensitize                  SMALLINT     NOT NULL DEFAULT 0,
    sql_injection_check          SMALLINT     NOT NULL DEFAULT 1,
    align                        SMALLINT     NOT NULL DEFAULT 0,
    width                        INTEGER               DEFAULT 120,
    show_order                   INTEGER               DEFAULT 1,
    max_length                   INTEGER               DEFAULT 50,
    min_length                   INTEGER               DEFAULT 1,
    max                          INTEGER               DEFAULT 2147483647,
    min                          INTEGER               DEFAULT 0,
    precision                    INTEGER               DEFAULT 0,
    multiple                     SMALLINT     NOT NULL DEFAULT 0,
    pattern                      SMALLINT     NOT NULL DEFAULT -1,
    relation_type                SMALLINT     NOT NULL DEFAULT 1,
    external_class               VARCHAR(255)          DEFAULT NULL,
    external_class_primary_field VARCHAR(80)           DEFAULT NULL,
    inner_primary_field          VARCHAR(80)           DEFAULT NULL,
    external_class_display_field VARCHAR(80)           DEFAULT NULL,
    external_class_auto_fill     BOOLEAN   NOT NULL DEFAULT 0,
    field_component              VARCHAR(512)          DEFAULT NULL,
    column_component             VARCHAR(512)          DEFAULT NULL,
    extras_params                VARCHAR(512)          DEFAULT NULL,
    creator_id                   VARCHAR(64)  NOT NULL,
    created_by                   VARCHAR(64)  NOT NULL,
    created_time                 TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updater_id                   VARCHAR(64)           DEFAULT NULL,
    updated_by                   VARCHAR(64)           DEFAULT NULL,
    updated_time                 TIMESTAMP             DEFAULT NULL,
    version                      INTEGER               DEFAULT 1
);

-- ====================== PG 专用：独立添加字段注释 ======================
COMMENT ON TABLE "platform_field_define" IS '字段定义';
COMMENT ON COLUMN "platform_field_define"."id" IS '编号';
COMMENT ON COLUMN "platform_field_define"."class_name" IS '类';
COMMENT ON COLUMN "platform_field_define"."name" IS '属性名';
COMMENT ON COLUMN "platform_field_define"."field_name" IS '字段名';
COMMENT ON COLUMN "platform_field_define"."description" IS '说明';
COMMENT ON COLUMN "platform_field_define"."display_name" IS '显示名';
COMMENT ON COLUMN "platform_field_define"."type" IS '字段类型';
COMMENT ON COLUMN "platform_field_define"."type_class_name" IS '字段类名';
COMMENT ON COLUMN "platform_field_define"."group_id" IS '分组';
COMMENT ON COLUMN "platform_field_define"."is_primary" IS '是否主键';
COMMENT ON COLUMN "platform_field_define"."is_show" IS '是否显示';
COMMENT ON COLUMN "platform_field_define"."is_overflow" IS '是否省略';
COMMENT ON COLUMN "platform_field_define"."is_i18n" IS '是否多语言';
COMMENT ON COLUMN "platform_field_define"."show_order" IS '显示顺序';
COMMENT ON COLUMN "platform_field_define"."width" IS '显示宽度';
COMMENT ON COLUMN "platform_field_define"."is_require" IS '是否必填';
COMMENT ON COLUMN "platform_field_define"."is_form" IS '是否表单';
COMMENT ON COLUMN "platform_field_define"."textarea_rows" IS '文本行数';
COMMENT ON COLUMN "platform_field_define"."is_sort" IS '是否排序';
COMMENT ON COLUMN "platform_field_define"."is_search" IS '是否搜索';
COMMENT ON COLUMN "platform_field_define"."is_fixed" IS '是否固定';
COMMENT ON COLUMN "platform_field_define"."encrypt" IS '是否加密';
COMMENT ON COLUMN "platform_field_define"."desensitize" IS '是否脱敏';
COMMENT ON COLUMN "platform_field_define"."sql_injection_check" IS '是否SQL注入检查';
COMMENT ON COLUMN "platform_field_define"."align" IS '对齐方式';
COMMENT ON COLUMN "platform_field_define"."max_length" IS '最大长度';
COMMENT ON COLUMN "platform_field_define"."min_length" IS '最小长度';
COMMENT ON COLUMN "platform_field_define"."max" IS '最大值';
COMMENT ON COLUMN "platform_field_define"."min" IS '最小值';
COMMENT ON COLUMN "platform_field_define"."precision" IS '精度';
COMMENT ON COLUMN "platform_field_define"."multiple" IS '是否多选';
COMMENT ON COLUMN "platform_field_define"."pattern" IS '校验规则';
COMMENT ON COLUMN "platform_field_define"."relation_type" IS '关联类型';
COMMENT ON COLUMN "platform_field_define"."external_class" IS '关联模型';
COMMENT ON COLUMN "platform_field_define"."external_class_primary_field" IS '关联模型字段';
COMMENT ON COLUMN "platform_field_define"."inner_primary_field" IS '明细表关联字段';
COMMENT ON COLUMN "platform_field_define"."external_class_display_field" IS '关联模型展示字段';
COMMENT ON COLUMN "platform_field_define"."external_class_auto_fill" IS '接口自动填充';
COMMENT ON COLUMN "platform_field_define"."field_component" IS '表单自定义组件';
COMMENT ON COLUMN "platform_field_define"."column_component" IS '表格列自定义组件';

-- 父类AuditEntity审计字段
COMMENT ON COLUMN "platform_field_define".created_by IS '创建人ID';
COMMENT ON COLUMN "platform_field_define".created_time IS '创建时间';
COMMENT ON COLUMN "platform_field_define".updated_by IS '更新人ID';
COMMENT ON COLUMN "platform_field_define".updated_time IS '更新时间';
COMMENT ON COLUMN "platform_field_define".version IS '乐观锁版本号';

-- 可选：添加索引优化查询性能
ALTER TABLE "platform_field_define"
    ADD CONSTRAINT uk_platform_field_define_class_name_name UNIQUE (class_name, name);

ALTER TABLE platform_field_define
    ADD CONSTRAINT fk_platform_field_define_class_name
        FOREIGN KEY (class_name)
            REFERENCES platform_entity_define(code) ON DELETE CASCADE;
