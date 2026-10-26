package com.byteasy.platform.vo;

import com.byteasy.platform.entity.ResourceCategory;
import com.byteasy.platform.entity.ResourceGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
@Schema(description = "领域分类")
public class ResourceCategoryVO extends ResourceCategory {
    @Serial
    private static final long serialVersionUID = -5054021435558438497L;

    @Schema(description = "领域分组")
    private List<ResourceCategoryVO> children;

    @Schema(description = "资源分组")
    private List<ResourceGroup> resourceGroups;
}
