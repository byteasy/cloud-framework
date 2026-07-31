package com.byteasy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class TreeNodeDTO {

    private Object id;
    private Object parentId;
    private String icon;
    private String type;
    private String code;
    private String name;
    private Boolean leaf;
    private Collection<TreeNodeDTO> children;
}
