package com.byteasy.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
@Schema(name = "LeftMenuVO", description = "左侧菜单")
public class LeftMenuVO extends ResourceVO {
    @Serial
    private static final long serialVersionUID = -8891338306414306803L;

    @Schema(description = "子菜单")
    private List<LeftMenuVO> childrenList;
}
