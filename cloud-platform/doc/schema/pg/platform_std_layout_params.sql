CREATE TABLE platform_std_layout_params
(
    id                                   BIGSERIAL PRIMARY KEY,
    class_name                           VARCHAR(255),

    api_query                            VARCHAR(512),
    api_cascade                          VARCHAR(512),
    api_save                             VARCHAR(512),
    api_delete                           VARCHAR(512),
    api_download_import_template         VARCHAR(512),
    api_import_preview                   VARCHAR(512),
    api_import_batch_save                VARCHAR(512),
    api_export                           VARCHAR(512),

    title_column_name                    VARCHAR(255)         default 'name',
    query_params                         VARCHAR(1024),
    form_width                           VARCHAR(64)          DEFAULT NULL,
    form_component                       VARCHAR(255)         DEFAULT NULL,

    operation_column_width               VARCHAR(64)          DEFAULT NULL,
    pagination_visible                   BOOLEAN              DEFAULT TRUE,
    query_form_visible                   BOOLEAN              DEFAULT TRUE,
    dialog_model                         BOOLEAN              DEFAULT FALSE,
    show_summary                         BOOLEAN              DEFAULT TRUE,
    excel_import_component               VARCHAR(255)         DEFAULT NULL,
    excel_export_component               VARCHAR(255)         DEFAULT NULL,
    query_form_component                 VARCHAR(255)         DEFAULT NULL,
    query_form_extra_component           VARCHAR(255)         DEFAULT NULL,
    query_form_buttons_component         VARCHAR(255)         DEFAULT NULL,
    query_form_button_extra_component    VARCHAR(255)         DEFAULT NULL,
    tool_buttons_extra_component         VARCHAR(255)         DEFAULT NULL,
    more_action_extra_component          VARCHAR(255)         DEFAULT NULL,
    operation_column_component           VARCHAR(255)         DEFAULT NULL,
    operation_column_extra_component     VARCHAR(255)         DEFAULT NULL,
    operation_column_extra_end_component VARCHAR(255)         DEFAULT NULL,
    batch_add_extra_tool_component       VARCHAR(255)         DEFAULT NULL,
    row_key                              VARCHAR(64),
    tree_props                           VARCHAR(1024),
    default_expand_all                   BOOLEAN              DEFAULT FALSE,
    show_lang                            VARCHAR(64),

    creator_id                           VARCHAR(64) NOT NULL,
    created_by                           VARCHAR(64) NOT NULL,
    created_time                         TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updater_id                           VARCHAR(64)          DEFAULT NULL,
    updated_by                           VARCHAR(64)          DEFAULT NULL,
    updated_time                         TIMESTAMP            DEFAULT NULL,
    version                              INTEGER              DEFAULT 1
);

COMMENT ON TABLE platform_std_layout_params IS '标准布局参数';
COMMENT ON COLUMN platform_std_layout_params.id IS '主键';
COMMENT ON COLUMN platform_std_layout_params.class_name IS '类名';
COMMENT ON COLUMN platform_std_layout_params.api_query IS '查询API';
COMMENT ON COLUMN platform_std_layout_params.api_cascade IS '字段联动API';
COMMENT ON COLUMN platform_std_layout_params.api_save IS '保存/新增API';
COMMENT ON COLUMN platform_std_layout_params.api_delete IS '删除API';
COMMENT ON COLUMN platform_std_layout_params.api_download_import_template IS '下载导入模板API';
COMMENT ON COLUMN platform_std_layout_params.api_import_preview IS '导入预览API';
COMMENT ON COLUMN platform_std_layout_params.api_import_batch_save IS '批量导入保存API';
COMMENT ON COLUMN platform_std_layout_params.api_export IS '导出API';
COMMENT ON COLUMN platform_std_layout_params.title_column_name IS '卡片标题列';
COMMENT ON COLUMN platform_std_layout_params.query_params IS '默认查询参数';
COMMENT ON COLUMN platform_std_layout_params.form_width IS '表单页面宽度';
COMMENT ON COLUMN platform_std_layout_params.operation_column_width IS '操作列宽度';
COMMENT ON COLUMN platform_std_layout_params.pagination_visible IS '是否启用分页';
COMMENT ON COLUMN platform_std_layout_params.query_form_visible IS '是否显示查询表单';
COMMENT ON COLUMN platform_std_layout_params.form_component IS '自定义表单组件';
COMMENT ON COLUMN platform_std_layout_params.excel_import_component IS '自定义表格导入组件';
COMMENT ON COLUMN platform_std_layout_params.excel_export_component IS '自定义表格导出组件';
COMMENT ON COLUMN platform_std_layout_params.query_form_extra_component IS '查询栏组件';
COMMENT ON COLUMN platform_std_layout_params.operation_column_component IS '操作列组件';
COMMENT ON COLUMN platform_std_layout_params.operation_column_extra_component IS '操作列编辑后控件';
COMMENT ON COLUMN platform_std_layout_params.operation_column_extra_end_component IS '操作列删除后控件';
COMMENT ON COLUMN platform_std_layout_params.tool_buttons_extra_component IS '工具栏按钮拓展组件';
COMMENT ON COLUMN platform_std_layout_params.more_action_extra_component IS '工具栏更多菜单拓展组件';
COMMENT ON COLUMN platform_std_layout_params.batch_add_extra_tool_component IS '批量添加工具栏拓展组件';
COMMENT ON COLUMN platform_std_layout_params.dialog_model IS '对话框';
COMMENT ON COLUMN platform_std_layout_params.show_lang IS '显示语言';
COMMENT ON COLUMN platform_std_layout_params.show_summary IS '显示合计';
COMMENT ON COLUMN platform_std_layout_params.row_key IS '行主键';
COMMENT ON COLUMN platform_std_layout_params.default_expand_all IS '默认展开';
COMMENT ON COLUMN platform_std_layout_params.tree_props IS '树形数据配置';

COMMENT ON COLUMN platform_std_layout_params.creator_id IS '创建人编号';
COMMENT ON COLUMN platform_std_layout_params.created_by IS '创建人姓名';
COMMENT ON COLUMN platform_std_layout_params.created_time IS '创建时间';
COMMENT ON COLUMN platform_std_layout_params.updater_id IS '更新人编号';
COMMENT ON COLUMN platform_std_layout_params.updated_by IS '更新人姓名';
COMMENT ON COLUMN platform_std_layout_params.updated_time IS '更新时间';
COMMENT ON COLUMN platform_std_layout_params.version IS '版本号';

ALTER TABLE platform_std_layout_params
    ADD CONSTRAINT fk_platform_std_layout_params_class_name
        FOREIGN KEY (class_name)
            REFERENCES platform_entity_define(code) ON DELETE CASCADE;