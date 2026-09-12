package com.byteasy.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.byteasy.annotations.FieldMeta;
import com.byteasy.entity.AuditEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * JS脚本
 * </p>
 *
 * @author devops
 * @since 2026-09-12
 */
@Getter
@Setter
@TableName("platform_js_script")
@FieldMeta(name = "JsScript", description = "JS脚本")
public class JsScript extends AuditEntity<Long> {

    @FieldMeta(name = "名称", showOrder = 100)
    @TableField("name")
    private String name;

    @FieldMeta(name = "脚本", showOrder = 101)
    @TableField("script")
    private String script;
}
