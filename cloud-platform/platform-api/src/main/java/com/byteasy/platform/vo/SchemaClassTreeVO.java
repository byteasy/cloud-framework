package com.byteasy.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "服务类视图")
public class SchemaClassTreeVO {

    @Schema(description = "菜单资源")
    private String name;
    @Schema(description = "类名")
    private List<SchemaClassTreeVO> children;
}
