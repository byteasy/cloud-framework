package com.byteasy.common.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteasy.common.mapper.IAuditLogMapper;
import com.byteasy.common.service.IAuditLogService;
import com.byteasy.entity.AuditLog;
import com.byteasy.platform.entity.EntityDefine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
public class AuditLogServiceImpl implements IAuditLogService {

    @Autowired
    private IAuditLogMapper auditLogMapper;

    @Override
    public void batchInsert(EntityDefine entityDefine, Collection<AuditLog> auditLogs) {
        auditLogMapper.batchInsert(entityDefine.getTableName() + "_audit_log", auditLogs);
    }

    @Override
    public Page<AuditLog> page(EntityDefine entityDefine, String entityId, LocalDateTime start, LocalDateTime end, long current, long pageSize) {
        List<AuditLog> list = auditLogMapper.queryForList(entityDefine.getTableName() + "_audit_log", entityId, start, end, current, pageSize);
        long total = auditLogMapper.countForPage(entityDefine.getTableName() + "_audit_log", entityId, start, end);
        return new Page<AuditLog>().setRecords(list).setTotal(total);
    }
}
