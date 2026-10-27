package com.byteasy.platform.dto;

import com.byteasy.platform.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Schema(description = "用户角色分配视图")
public class RoleUserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -477024443976478658L;

    @Schema(description = "分配此角色的用户")
    private Set<String> userNames;

    @Schema(description = "当前角色")
    private Role role;
}
