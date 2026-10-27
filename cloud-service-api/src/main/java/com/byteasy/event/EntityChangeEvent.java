package com.byteasy.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 实体变更事件，可用于清空 Infinispan 缓存、发送 MQ 或记录审计日志
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityChangeEvent {

    public enum ChangeType {
        INSERT,
        UPDATE,
        DELETE
    }

    /**
     * 变更类型
     */
    private ChangeType changeType;

    /**
     * 实体类型 (如 User.class)
     */
    private Class<?> entityClass;

    /**
     * 影响的主键列表
     */
    @Builder.Default
    private List<Serializable> ids = Collections.emptyList();

    /**
     * 变更前的实体数据列表 (UPDATE / DELETE 时可能存在)
     */
    @Builder.Default
    private List<Object> beforeEntities = Collections.emptyList();

    /**
     * 变更后的实体数据列表 (INSERT / UPDATE 时存在)
     */
    @Builder.Default
    private List<Object> afterEntities = Collections.emptyList();

    /**
     * 是否是基于 Wrapper 条件触发的批量操作
     */
    private boolean wrapperOperation;
}
