package com.byteasy.dto;

import com.byteasy.entity.AuditEntity;
import com.byteasy.enums.Enable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceInstanceDTO extends AuditEntity<Long> {

    @Schema(description = "编码")
    private String code;

    @Schema(description = "路径")
    private String contextPath;

    @Schema(description = "状态")
    private Enable status;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "备注")
    private String remark;
}
