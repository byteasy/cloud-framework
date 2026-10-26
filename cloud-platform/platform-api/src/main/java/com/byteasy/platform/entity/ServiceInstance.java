package com.byteasy.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.byteasy.annotations.FieldMeta;
import com.byteasy.entity.AuditEntity;
import com.byteasy.enums.Enable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * <p>
 * 平台服务定义表
 * </p>
 *
 * @author devops
 * @since 2026-03-03
 */
@Getter
@Setter
@TableName("platform_service_instance")
@FieldMeta(name = "ServiceInstance", description = "平台服务定义表")
public class ServiceInstance extends AuditEntity<Long> {
    @Serial
    private static final long serialVersionUID = 5022182471975121962L;

    @TableField("code")
    @FieldMeta(showOrder = 100, name = "编码", isSearch = true, isRequire = true)
    private String code;

    @TableField("context_path")
    @FieldMeta(showOrder = 101, name = "路径", isSearch = true)
    private String contextPath;

    @TableField("status")
    @FieldMeta(showOrder = 102, name = "状态", isSearch = true, isRequire = true)
    private Enable status;

    @TableField("name")
    @FieldMeta(showOrder = 103, name = "名称", isSearch = true, isI18n = true, isRequire = true)
    private String name;

    @TableField("remark")
    @FieldMeta(showOrder = 104, name = "备注")
    private String remark;

    @TableField("category_id")
    @FieldMeta(showOrder = 105, name = "分类编号")
    private Long categoryId;
}
