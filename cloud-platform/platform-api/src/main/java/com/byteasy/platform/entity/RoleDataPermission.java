package com.byteasy.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.byteasy.entity.Entity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@TableName("COM_ROLE_DATA_PERMISSION")
public class RoleDataPermission extends Entity<Long> {

    @Schema(description = "角色外键")
    @TableField("ROLE_ID")
    private Long roleId;

    @Schema(description = "数据权限策略外键")
    @TableField("DATA_PERMISSION_ID")
    private Long dataPermissionId;
}
