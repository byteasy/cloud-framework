package com.byteasy.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.byteasy.annotations.FieldMeta;
import com.byteasy.entity.AuditEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * <p>
 * 标准布局参数
 * </p>
 *
 * @author devops
 * @since 2026-09-19
 */
@Getter
@Setter
@TableName("platform_std_layout_params")
@FieldMeta(name = "StdLayoutParams", description = "标准布局参数")
public class StdLayoutParams extends AuditEntity<Long> {
    @Serial
    private static final long serialVersionUID = -3140046379847112734L;

    @FieldMeta(name = "类名", showOrder = 100)
    @TableField("class_name")
    private String className;

    @FieldMeta(name = "查询API", showOrder = 101)
    @TableField("api_query")
    private String apiQuery;

    @FieldMeta(name = "保存/新增API", showOrder = 102)
    @TableField("api_save")
    private String apiSave;

    @FieldMeta(name = "删除API", showOrder = 103)
    @TableField("api_delete")
    private String apiDelete;

    @FieldMeta(name = "下载导入模板API", showOrder = 104)
    @TableField("api_download_import_template")
    private String apiDownloadImportTemplate;

    @FieldMeta(name = "导入预览API", showOrder = 105)
    @TableField("api_import_preview")
    private String apiImportPreview;

    @FieldMeta(name = "批量导入保存API", showOrder = 106)
    @TableField("api_import_batch_save")
    private String apiImportBatchSave;

    @FieldMeta(name = "导出API", showOrder = 107)
    @TableField("api_export")
    private String apiExport;

    @FieldMeta(name = "卡片标题列", showOrder = 108)
    @TableField("title_column_name")
    private String titleColumnName;

    @FieldMeta(name = "默认查询参数", showOrder = 109)
    @TableField("query_params")
    private String queryParams;

    @FieldMeta(name = "表单页面宽度", showOrder = 110)
    @TableField("form_width")
    private String formWidth;

    @FieldMeta(name = "自定义表单组件", showOrder = 111)
    @TableField("form_component")
    private String formComponent;

    @FieldMeta(name = "操作列宽度", showOrder = 112)
    @TableField("operation_column_width")
    private String operationColumnWidth;

    @FieldMeta(name = "是否启用分页", showOrder = 113)
    @TableField("pagination_visible")
    private Boolean paginationVisible;

    @FieldMeta(name = "是否显示查询表单", showOrder = 114)
    @TableField("query_form_visible")
    private Boolean queryFormVisible;

    @FieldMeta(name = "对话框/抽屉", showOrder = 115)
    @TableField("dialog_model")
    private Boolean dialogModel;

    @FieldMeta(name = "是否在表尾显示合计行", showOrder = 116)
    @TableField("show_summary")
    private Boolean showSummary;

    @FieldMeta(name = "自定义表格导入组件", showOrder = 117)
    @TableField("excel_import_component")
    private String excelImportComponent;

    @FieldMeta(name = "自定义表格导出组件", showOrder = 118)
    @TableField("excel_export_component")
    private String excelExportComponent;

    @FieldMeta(name = "查询栏自定义组件", showOrder = 119)
    @TableField("query_form_extra_component")
    private String queryFormExtraComponent;

    @FieldMeta(name = "操作列编辑后控件", showOrder = 120)
    @TableField("operation_column_extra_component")
    private String operationColumnExtraComponent;

    @FieldMeta(name = "行主键", showOrder = 121)
    @TableField("row_key")
    private String rowKey;

    @FieldMeta(name = "树形数据配置", showOrder = 122)
    @TableField("tree_props")
    private String treeProps;

    @FieldMeta(name = "默认展开", showOrder = 123)
    @TableField("default_expand_all")
    private Boolean defaultExpandAll;

    @FieldMeta(name = "显示语言", showOrder = 124)
    @TableField("show_lang")
    private String showLang;

    @FieldMeta(name = "操作列组件", showOrder = 125)
    @TableField("operation_column_component")
    private String operationColumnComponent;

    @FieldMeta(name = "操作列删除后控件", showOrder = 126)
    @TableField("operation_column_extra_end_component")
    private String operationColumnExtraEndComponent;

    @FieldMeta(name = "工具栏按钮拓展组件", showOrder = 127)
    @TableField("tool_buttons_extra_component")
    private String toolButtonsExtraComponent;

    @FieldMeta(name = "工具栏更多菜单拓展组件", showOrder = 128)
    @TableField("more_action_extra_component")
    private String moreActionExtraComponent;

    @FieldMeta(name = "批量选择", showOrder = 129)
    @TableField("multiple")
    private Boolean multiple;

    @FieldMeta(name = "批量删除接口", showOrder = 130)
    @TableField("api_batch_delete")
    private String apiBatchDelete;

    @FieldMeta(name = "批量添加工具栏拓展组件", showOrder = 131)
    @TableField("batch_add_extra_tool_component")
    private String batchAddExtraToolComponent;

    @FieldMeta(name = "表单字段联动api", showOrder = 132)
    @TableField("api_cascade")
    private String apiCascade;

    @FieldMeta(name = "表单类型", showOrder = 133)
    @TableField("form_type")
    private Integer formType;

    @FieldMeta(name = "页面编号", showOrder = 133)
    @TableField("eb_page_id")
    private Long ebPageId;
}
