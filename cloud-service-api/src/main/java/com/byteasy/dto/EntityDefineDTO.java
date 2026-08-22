package com.byteasy.dto;

import com.byteasy.annotations.FieldMeta;
import com.byteasy.entity.AuditEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 平台模型定义
 * </p>
 *
 * @author devops
 * @since 2026-05-21
 */
@Getter
@Setter
@Schema(name = "EntityDefine", description = "平台模型定义")
public class EntityDefineDTO extends AuditEntity<Long> {

    @Schema(description = "编码")
    private String code;
    @Schema(name = "菜单编号")
    private Long resourceId;
    @Schema(name = "菜单编号")
    private Long stdLayoutParamsId;
    @Schema(description = "服务编号")
    private String serviceCode;
    @Schema(description = "名称")
    private String name;
    @Schema(description = "表名")
    private String tableName;
    @Schema(description = "说明")
    private String remark;
}
