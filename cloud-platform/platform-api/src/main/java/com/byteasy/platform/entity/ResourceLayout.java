package com.byteasy.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.byteasy.annotations.FieldMeta;
import com.byteasy.entity.AuditEntity;
import com.byteasy.enums.IBaseEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 页面布局
 * </p>
 *
 * @author devops
 * @since 2026-09-19
 */
@Getter
@Setter
@TableName("platform_resource_layout")
@FieldMeta(name = "ResourceLayout", description = "页面布局")
public class ResourceLayout extends AuditEntity<Long> {

    @FieldMeta(name = "菜单", showOrder = 100)
    @TableField("resource_id")
    private Long resourceId;

    @FieldMeta(name = "类型", showOrder = 101)
    @TableField("type")
    private Type type;

    @FieldMeta(name = "标准布局参数", showOrder = 102)
    @TableField("std_layout_params_id")
    private Long stdLayoutParamsId;

    @FieldMeta(name = "组件路径", showOrder = 103)
    @TableField("component_path")
    private String componentPath;

    @FieldMeta(name = "组件参数", showOrder = 104)
    @TableField("component_param")
    private String componentParam;

    @FieldMeta(name = "默认布局", showOrder = 105)
    @TableField("is_default")
    private Boolean isDefault;

    @FieldMeta(name = "名称", showOrder = 106)
    @TableField("name")
    private String name;

    @FieldMeta(name = "公开访问", showOrder = 107)
    @TableField("is_public")
    private Boolean isPublic;

    @FieldMeta(name = "低代码页面", showOrder = 9)
    @TableField("eb_page_id")
    private Long ebPageId;

    public enum Type implements IBaseEnum<Integer> {

        STAND(0, "标准布局"), EB(1, "EB布局"), DEVELOP(2, "定开布局");

        public final int value;
        public final String desc;

        Type(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @Override
        public Integer getValue() {
            return this.value;
        }

        public static Type fromValue(int value) {
            for (Type status : Type.values()) {
                if (status.value == value) {
                    return status;
                }
            }
            throw new IllegalArgumentException("无效的状态值: " + value);
        }

        @Override
        public String getName() {
            return desc;
        }
    }
}
