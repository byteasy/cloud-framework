package com.byteasy.event;

import com.byteasy.entity.Entity;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 实体变更事件类，用于描述数据实体的变更操作
 *
 * @param <T> 实体类型，需继承BaseEntity
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class EntityChangeSource<T extends Entity> extends EventSource implements Serializable {

    public enum ChangeType {
        CREATE, UPDATE, DELETE, BATCH_CREATE, BATCH_UPDATE, BATCH_DELETE
    }

    private String eventId;
    private ChangeType changeType;
    /**
     * 变更前
     */
    private T before;
    /**
     * 变更后
     */
    private T after;

    /**
     * 数据发起的变更系统
     */
    private String sourceSystem;

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
    public EntityChangeSource(ChangeType changeType, T before, T after) {
        this();
        this.changeType = changeType;
        this.before = before;
        this.after = after;
    }

    public static <T extends Entity> EntityChangeSource<T> of(ChangeType changeType, T before, T after) {
        return new EntityChangeSource<>(changeType, before, after);
    }

    @JsonAnySetter
    public void addExtendedField(String key, Object value) {
        this.extendedFields.put(key, value);
    }

}