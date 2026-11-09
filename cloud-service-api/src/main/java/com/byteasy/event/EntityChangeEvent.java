package com.byteasy.event;

import com.byteasy.entity.AuditLog;
import com.byteasy.enums.IBaseEnum;
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

    public enum ChangeType implements IBaseEnum<Integer> {
        INSERT(0, "新增"), UPDATE(1, "修改"), DELETE(2, "删除"),
        ;
        public final Integer value;
        public final String desc;

        ChangeType(Integer value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @Override
        public Integer getValue() {
            return this.value;
        }

        public static ChangeType fromValue(String value) {
            for (ChangeType status : ChangeType.values()) {
                if (status.value.equals(value)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("无效的枚举值: " + value);
        }

        @Override
        public String getName() {
            return desc;
        }
    }

    /**
     * 变更类型
     */
    private ChangeType changeType;

    /**
     * 实体类型 (如 User.class)
     */
    private String entityClass;

    /**
     * 变更前的实体数据列表 (UPDATE / DELETE 时可能存在)
     */
    @Builder.Default
    private List<?> beforeEntities = Collections.emptyList();

    /**
     * 变更后的实体数据列表 (INSERT / UPDATE 时存在)
     */
    @Builder.Default
    private List<?> afterEntities = Collections.emptyList();

    /**
     * 是否是基于 Wrapper 条件触发的批量操作
     */
    private boolean wrapperOperation = false;
}
