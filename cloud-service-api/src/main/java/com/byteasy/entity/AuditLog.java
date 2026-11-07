package com.byteasy.entity;

import com.byteasy.annotations.FieldMeta;
import com.byteasy.enums.IBaseEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditLog extends ImmutablyEntity<Long> {
    @FieldMeta(description = "实体主键")
    private Long entityId;
    @FieldMeta(description = "变更类型")
    private ChangeType changeType;
    @FieldMeta(description = "最新内容")
    private String entityContent;


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
            throw new IllegalArgumentException("无效的状态值: " + value);
        }

        @Override
        public String getName() {
            return desc;
        }
    }
}
