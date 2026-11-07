package com.byteasy.platform.dto;

import com.byteasy.platform.entity.EntityDefine;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class CreateAuditLogTableDTO extends EntityDefine {
    public CreateAuditLogTableDTO() {
    }

    public CreateAuditLogTableDTO(EntityDefine entityDefine) {
        if (entityDefine != null) {
            // 使用 Spring 框架自带的工具类，将 entityDefine 中的属性值复制到当前对象 (this) 中
            BeanUtils.copyProperties(entityDefine, this);
        }
    }
}
