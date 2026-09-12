package com.byteasy.platform.vo;

import com.byteasy.platform.entity.ResourceGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Schema(description = "顶部菜单导航")
@Getter
@Setter
public class TopMenuVO {
    @Schema(description = "快捷菜单组")
    private List<ResourceGroup> resourceGroups;

    @Schema(description = "拓展菜单")
    private List<ResourceCategoryVO> resourceCategoryVOS;
}
