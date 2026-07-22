package com.byteasy.event;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 实体变更事件类，用于描述数据实体的变更操作
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class EntityChangeSource extends EventSource implements Serializable {

    public enum ChangeType {
        CREATE, UPDATE, DELETE, BATCH_CREATE, BATCH_UPDATE, BATCH_DELETE
    }

    private String eventId;
    private ChangeType changeType;
    /**
     * 变更前
     */
    private Object before;
    /**
     * 变更后
     */
    private Object after;

    /**
     * 变更实体类
     */
    private String className;

    /**
     * 变更服务
     */
    private String srcApplicationName;

    /**
     * 拓展字段
     */
    private Map<String, Object> extendedFields;

    // 无参构造方法（Jackson反序列化需要）
    public EntityChangeSource() {
        this.eventId = UUID.randomUUID().toString();
        this.extendedFields = new HashMap<>();
    }

    // 全参数构造方法
    public EntityChangeSource(ChangeType changeType, Object before, Object after) {
        this();
        this.changeType = changeType;
        this.before = before;
        this.after = after;
    }

    public EntityChangeSource(ChangeType changeType, Object before, Object after, String className) {
        this.changeType = changeType;
        this.before = before;
        this.after = after;
        this.className = className;
    }

    public static EntityChangeSource of(ChangeType changeType, Object before, Object after) {
        return new EntityChangeSource(changeType, before, after);
    }

    public static EntityChangeSource of(ChangeType changeType, Object before, Object after, String className) {
        return new EntityChangeSource(changeType, before, after, className);
    }

    @JsonAnySetter
    public void addExtendedField(String key, Object value) {
        this.extendedFields.put(key, value);
    }

}