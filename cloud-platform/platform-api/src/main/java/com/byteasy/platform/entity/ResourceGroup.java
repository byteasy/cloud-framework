package com.byteasy.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.byteasy.annotations.FieldMeta;
import com.byteasy.entity.AuditEntity;
import com.byteasy.enums.YesNo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 平台资源分组定义表（用于对资源进行分类管理，如：系统管理、业务管理、报表管理等）
 * </p>
 *
 * @author devops
 * @since 2026-03-03
 */
@Getter
@Setter
@TableName("platform_resource_group")
@Schema(name = "ResourceGroup", description = "{\"zh_CN\":\"菜单分组\",\"en\":\"Menu Group\"}")
public class ResourceGroup extends AuditEntity<Long> {

    @Schema(description = "领域分类")
    @TableField("category_id")
    @FieldMeta(isRequire = true, showOrder = 101, isShow = false)
    private Long categoryId;

    @Schema(description = "编码")
    @TableField("code")
    @FieldMeta(showOrder = 102, isSearch = true, isRequire = true, minLength = 2, maxLength = 50)
    private String code;

    @Schema(description = "服务编码")
    @TableField("service_code")
    @FieldMeta(showOrder = 102, isSearch = true, isRequire = true)
    private String serviceCode;

    @Schema(description = "名称")
    @TableField("name")
    @FieldMeta(showOrder = 103, isSearch = true, isRequire = true, isI18n = true)
    private String name;

    @Schema(description = "显示顺序")
    @TableField("show_order")
    @FieldMeta(showOrder = 104, isRequire = true, width = 200)
    private Integer showOrder;

    @Schema(description = "路由")
    @TableField(exist = false)
    @FieldMeta(showOrder = 105, isRequire = false, isForm = false, isShow = false)
    private String router;

    @Schema(description = "快捷菜单")
    @TableField("navbar_menu")
    @FieldMeta(showOrder = 106, isRequire = true)
    private YesNo navbarMenu;

    @Schema(description = "是否微应用")
    @TableField("is_microapp")
    @FieldMeta(showOrder = 107, isRequire = true)
    private YesNo isMicroapp;

    @Schema(description = "入口地址")
    @TableField("entry")
    @FieldMeta(showOrder = 108, isRequire = true, maxLength = 512)
    private String entry;

    @Schema(description = "拓展属性")
    @TableField("props")
    @FieldMeta(showOrder = 109, isRequire = false, maxLength = 512)
    private String props;

    @Schema(description = "备注")
    @TableField("remark")
    @FieldMeta(showOrder = 110, isRequire = false, maxLength = 512)
    private String remark;
}
