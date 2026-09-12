package com.byteasy.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@Schema(description = "权限分配展示视图")
public class RolePermissionVO {

    @Schema(description = "菜单资源")
    private Collection<LeftMenuVO> leftMenuVOS;

    @Schema(description = "权限集合")
    private Set<Long> permissionIds;

    @Schema(description = "当前角色")
    private Long roleId;

    @Schema(description = "当前应用")
    private Long groupId;
}
