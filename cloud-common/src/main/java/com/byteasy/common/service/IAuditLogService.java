package com.byteasy.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteasy.entity.AuditLog;
import com.byteasy.platform.entity.EntityDefine;

import java.time.LocalDateTime;
import java.util.Collection;

public interface IAuditLogService {
    void batchInsert(EntityDefine entityDefine, Collection<AuditLog> auditLogs);

    Page<AuditLog> page(EntityDefine entityDefine,
                        String entityId,
                        LocalDateTime start,
                        LocalDateTime end,
                        long current,
                        long pageSize);
}
