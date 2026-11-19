package com.byteasy.dto;

import com.byteasy.entity.AuditLog;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AuditLogDTO extends AuditLog {
    private String className;
    private Set<String> diffFields;
    private String oldEntityContent;
}
