package com.byteasy.platform.vo;

import com.byteasy.platform.entity.Resource;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "ResourceVO", description = "平台资源定义展示")
public class ResourceVO extends Resource {

    @Schema(description = "资源分组")
    private String groupName;

    @Schema(description = "父级资源")
    private String parentName;

    /**
     * 改为计算属性
     */
    @Schema(description = "路由")
    private String routerUrl;
}
