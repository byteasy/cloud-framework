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
 * 字段分组
 * </p>
 *
 * @author devops
 * @since 2026-09-15
 */
@Getter
@Setter
@TableName("platform_field_group")
@FieldMeta(name = "FieldGroup", description = "字段分组")
public class FieldGroup extends AuditEntity<Long> {
    @Serial
    private static final long serialVersionUID = 6157762309217086667L;

    @FieldMeta(name = "类", showOrder = 100)
    @TableField("class_name")
    private String className;

    @FieldMeta(name = "名称", showOrder = 101)
    @TableField("name")
    private String name;
}
