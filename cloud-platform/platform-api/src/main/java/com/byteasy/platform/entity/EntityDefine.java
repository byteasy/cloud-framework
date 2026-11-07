package com.byteasy.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.byteasy.annotations.FieldMeta;
import com.byteasy.entity.AuditEntity;
import com.byteasy.enums.IBaseEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * <p>
 * 平台模型定义
 * </p>
 *
 * @author devops
 * @since 2026-05-27
 */
@Getter
@Setter
@TableName("platform_entity_define")
@FieldMeta(name = "EntityDefine", description = "平台模型定义")
public class EntityDefine extends AuditEntity<Long> {
    @Serial
    private static final long serialVersionUID = 1184135931960492856L;

    @FieldMeta(name = "编码", showOrder = 100, maxLength = 512, isSearch = true)
    @TableField("code")
    private String code;

    @FieldMeta(name = "菜单编号", showOrder = 101)
    @TableField("resource_id")
    private Long resourceId;

    @FieldMeta(name = "布局参数", showOrder = 102)
    @TableField("std_layout_params_id")
    private Long stdLayoutParamsId;

    @FieldMeta(name = "服务", showOrder = 103, isSearch = true, externalClass = ServiceInstance.class, externalClassPrimaryField = "code")
    @TableField("service_code")
    private String serviceCode;

    @FieldMeta(name = "名称", showOrder = 104, maxLength = 512, isI18n = true, isSearch = true)
    @TableField("name")
    private String name;

    @FieldMeta(name = "数据库表名", showOrder = 105, maxLength = 1024)
    @TableField("table_name")
    private String tableName;

    @FieldMeta(name = "是否缓存", showOrder = 106)
    @TableField("cacheable")
    private Boolean cacheable;

    @FieldMeta(name = "是否跟踪变化", showOrder = 107)
    @TableField("trackable")
    private Boolean trackable;

    @FieldMeta(name = "是否审计", showOrder = 108)
    @TableField("auditable")
    private Boolean auditable;

    @FieldMeta(name = "可见性", showOrder = 109)
    @TableField("visibility")
    private Visibility visibility;

    @FieldMeta(name = "说明", showOrder = 110, maxLength = 1024)
    @TableField("remark")
    private String remark;

    public enum Visibility implements IBaseEnum<Integer> {
        PRIVATE(0, "私有"),
        PUBLIC(1, "公开"),
        ;
        public final Integer value;
        public final String desc;

        Visibility(Integer value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @Override
        public Integer getValue() {
            return this.value;
        }

        public static Visibility fromValue(String value) {
            for (Visibility status : Visibility.values()) {
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
