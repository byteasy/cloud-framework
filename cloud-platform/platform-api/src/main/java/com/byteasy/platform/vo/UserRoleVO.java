package com.byteasy.platform.vo;

import com.byteasy.platform.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@Schema(description = "用户角色分配视图")
public class UserRoleVO {

    @Schema(description = "角色")
    private Collection<Role> roles;

    @Schema(description = "用户拥有的角色")
    private Set<Long> roleIds;

    @Schema(description = "当前用户")
    private Long userId;
}
