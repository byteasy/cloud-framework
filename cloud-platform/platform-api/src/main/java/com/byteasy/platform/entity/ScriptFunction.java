package com.byteasy.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.byteasy.annotations.FieldMeta;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 脚本函数定义
 * </p>
 *
 * @author devops
 * @since 2026-09-11
 */
@Getter
@Setter
@TableName("platform_script_function")
@FieldMeta(name = "ScriptFunction", description = "脚本函数定义")
public class ScriptFunction implements Serializable {

    @FieldMeta(name = "服务编码", showOrder = 100)
    @TableField("service_code")
    private String serviceCode;

    @FieldMeta(name = "分组", showOrder = 101)
    @TableField("group_name")
    private String groupName;

    @FieldMeta(name = "名称", showOrder = 102)
    @TableField("name")
    private String name;

    @FieldMeta(name = "说明", showOrder = 103)
    @TableField("description")
    private String description;
}
