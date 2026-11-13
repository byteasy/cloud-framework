package com.byteasy.platform.dto;

import com.byteasy.platform.entity.ResourceCategory;
import com.byteasy.platform.entity.ResourceGroup;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public class ResourceGroupDTO extends ResourceGroup {
    @Serial
    private static final long serialVersionUID = 677641582852636088L;
    /**
     * 一级分类
     */
    private ResourceCategory rootCategory;
    /**
     * 二级分类
     */
    private ResourceCategory childCategory;

}
