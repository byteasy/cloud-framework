package com.byteasy.dto;

import com.byteasy.entity.AuditEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Function;

@Getter
@Setter
public class ScriptFunction extends AuditEntity<Long> {

    private String serviceCode;
    private String groupName;
    private String name;
    private String description;
    @JsonIgnore
    private Function<Object[], Object> function;
}
