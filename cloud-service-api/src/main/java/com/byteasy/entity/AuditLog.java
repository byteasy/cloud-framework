package com.byteasy.entity;

import com.byteasy.annotations.FieldMeta;
import com.byteasy.event.EntityChangeEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditLog extends ImmutablyEntity<Long> {
    @FieldMeta(description = "实体主键")
    private String entityId;
    @FieldMeta(description = "变更类型")
    private EntityChangeEvent.ChangeType changeType;
    @FieldMeta(description = "最新内容")
    private String entityContent;

}
