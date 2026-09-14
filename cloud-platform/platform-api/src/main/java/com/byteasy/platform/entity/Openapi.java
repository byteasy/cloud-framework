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
 * 平台接口定义
 * </p>
 *
 * @author devops
 * @since 2026-09-11
 */
@Getter
@Setter
@TableName("platform_openapi")
@FieldMeta(name = "Openapi", description = "平台接口定义")
public class Openapi extends AuditEntity<Long> {

    @FieldMeta(name = "编码", showOrder = 100)
    @TableField("code")
    private String code;

    @FieldMeta(name = "服务编码", showOrder = 101)
    @TableField("service_code")
    private String serviceCode;

    @FieldMeta(name = "接口名称", showOrder = 102)
    @TableField("name")
    private String name;

    @FieldMeta(name = "Http方法", showOrder = 103)
    @TableField("http_method")
    private HttpMethod httpMethod;

    @FieldMeta(name = "URI", showOrder = 104)
    @TableField("uri")
    private String uri;

    @FieldMeta(name = "函数脚本", showOrder = 105)
    @TableField("script_id")
    private Long scriptId;

    @FieldMeta(name = "说明", showOrder = 106)
    @TableField("remark")
    private String remark;

    @FieldMeta(name = "资源编号", showOrder = 107)
    @TableField("resource_id")
    private Long resourceId;

    @FieldMeta(name = "接口类型", showOrder = 108)
    @TableField("type")
    private Type type;

    @FieldMeta(name = "模型", showOrder = 109)
    @TableField("class_name")
    private String className;

    @FieldMeta(name = "权限控制类型", showOrder = 110)
    @TableField("permission_type")
    private PermissionType permissionType;

    @FieldMeta(name = "记录日志", showOrder = 111)
    @TableField("loggable")
    private Boolean loggable;

    @FieldMeta(name = "前置脚本钩子", showOrder = 112)
    @TableField("pre_script_id")
    private Long preScriptId;

    @FieldMeta(name = "后置脚本钩子", showOrder = 113)
    @TableField("post_script_id")
    private Long postScriptId;

    public enum PermissionType implements IBaseEnum<Integer> {
        PUBLIC(0, "开放访问"), LOGIN(1, "登录用户即可访问"), AUTH(2, "授权后才可访问");

        public final int value;
        public final String desc;

        PermissionType(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @Override
        public Integer getValue() {
            return this.value;
        }

        public static PermissionType fromValue(int value) {
            for (PermissionType status : PermissionType.values()) {
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

    public enum HttpMethod implements IBaseEnum<String> {
        GET("GET", "GET"), PUT("PUT", "PUT"), POST("POST", "POST"), DELETE("DELETE", "DELETE");

        public final String value;
        public final String desc;

        HttpMethod(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @Override
        public String getValue() {
            return this.value;
        }

        public static HttpMethod fromValue(String value) {
            for (HttpMethod status : HttpMethod.values()) {
                if (status.value.equals(value)) {
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

    public enum Type implements IBaseEnum<Integer> {
        PAGE(1, "分页查询"),
        SAVE_OR_UPDATE(2, "新增/保存"),
        REMOVE_BY_ENTITY(3, "主键删除"),
        DOWNLOAD_IMPORT_TEMPLATE(4, "下载导入模板"),
        IMPORT_PREVIEW(5, "导入预览"),
        SAVE_OR_UPDATE_BATCH(6, "批量导入"),
        EXPORT_EXCEL(7, "导出"),
        SCRIPT(8, "脚本"),
        GET_BY_PRIMARY_KEY(9, "主键查询"),
        LIST(10, "条件查询"),
        BATCH_DELETE(11, "批量删除"),
        DEVELOP(12, "自定义开发"),
        ;
        public final Integer value;
        public final String desc;

        Type(Integer value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @Override
        public Integer getValue() {
            return this.value;
        }

        public static Type fromValue(String value) {
            for (Type status : Type.values()) {
                if (status.value.equals(value)) {
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
