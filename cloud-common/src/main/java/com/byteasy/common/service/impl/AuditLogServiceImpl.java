package com.byteasy.common.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONNull;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteasy.common.mapper.IAuditLogMapper;
import com.byteasy.common.service.IAuditLogService;
import com.byteasy.dto.AuditLogDTO;
import com.byteasy.entity.AuditLog;
import com.byteasy.platform.entity.EntityDefine;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class AuditLogServiceImpl implements IAuditLogService {

    @Autowired
    private IAuditLogMapper auditLogMapper;

    @Override
    public void batchInsert(EntityDefine entityDefine, Collection<AuditLog> auditLogs) {
        auditLogMapper.batchInsert(entityDefine.getTableName() + "_audit_log", auditLogs);
    }

    @Override
    public Page<AuditLogDTO> page(EntityDefine entityDefine, String entityId, LocalDateTime start, LocalDateTime end, long current, long pageSize) {
        List<AuditLogDTO> list = auditLogMapper.queryForList(entityDefine.getTableName() + "_audit_log", entityId, start, end, current, pageSize);
        if (list.isEmpty()) {
            return new Page<AuditLogDTO>().setRecords(list).setTotal(0);
        }

        for (int i = 0; i < list.size() - 1; i++) {
            AuditLogDTO dto = list.get(i);
            AuditLogDTO dtoOld = list.get(i + 1);
            dto.setClassName(entityDefine.getCode());

            JSONObject currJson = parseJsonSafely(dto.getEntityContent());
            JSONObject oldJson = parseJsonSafely(dtoOld.getEntityContent());
            dto.setDiffFields(diffJson(currJson, oldJson));
            dto.setOldEntityContent(dtoOld.getEntityContent());
        }

        /**
         * 尝试设置最后一条，如果存在多页
         */
        AuditLogDTO dto = list.getLast();
        dto.setClassName(entityDefine.getCode());
        AuditLog pre = auditLogMapper.queryForLatestOne(entityDefine.getTableName() + "_audit_log", dto.getId());
        if (pre != null) {
            JSONObject currJson = parseJsonSafely(dto.getEntityContent());
            JSONObject oldJson = parseJsonSafely(pre.getEntityContent());
            dto.setDiffFields(diffJson(currJson, oldJson));
            dto.setOldEntityContent(pre.getEntityContent());
        }

        long total = auditLogMapper.countForPage(entityDefine.getTableName() + "_audit_log", entityId, start, end);
        return new Page<AuditLogDTO>().setRecords(list).setTotal(total);
    }

    /**
     * 对比两个 JSON 对象的差异字段（覆盖新增、删除、修改，处理数值精度与 null 差异）
     */
    private Set<String> diffJson(JSONObject latest, JSONObject older) {
        if (latest.isEmpty() && older.isEmpty()) {
            return Collections.emptySet();
        }
        Set<String> fields = Sets.newHashSet();
        // 关键：取两边的 key 并集，避免漏掉旧版有而新版被删除/置空的字段
        for (String key : Sets.union(latest.keySet(), older.keySet())) {
            Object v1 = normalizeValue(latest.get(key));
            Object v2 = normalizeValue(older.get(key));
            if (!isJsonValueEqual(v1, v2)) {
                fields.add(key);
            }
        }
        return fields;
    }

    private JSONObject parseJsonSafely(String content) {
        if (StrUtil.isBlank(content)) {
            return new JSONObject();
        }
        return JSONUtil.isTypeJSONObject(content) ? JSONUtil.parseObj(content) : new JSONObject();
    }

    private Object normalizeValue(Object val) {
        return (val instanceof JSONNull) ? null : val;
    }

    private boolean isJsonValueEqual(Object v1, Object v2) {
        if (v1 == v2) {
            return true;
        }
        if (v1 == null || v2 == null) {
            return false;
        }
        // 处理 Integer / Long / Double / BigDecimal 之间的等值比较（如 100 == 100L == 100.0）
        if (v1 instanceof Number && v2 instanceof Number) {
            return new BigDecimal(v1.toString()).compareTo(new BigDecimal(v2.toString())) == 0;
        }
        // 递归处理嵌套 JSONObject（避免嵌套对象内部也受 Integer/Long 或 JSONNull 影响）
        if (v1 instanceof JSONObject && v2 instanceof JSONObject) {
            JSONObject o1 = (JSONObject) v1;
            JSONObject o2 = (JSONObject) v2;
            for (String key : Sets.union(o1.keySet(), o2.keySet())) {
                if (!isJsonValueEqual(normalizeValue(o1.get(key)), normalizeValue(o2.get(key)))) {
                    return false;
                }
            }
            return true;
        }
        // 递归处理 JSONArray
        if (v1 instanceof JSONArray && v2 instanceof JSONArray) {
            JSONArray a1 = (JSONArray) v1;
            JSONArray a2 = (JSONArray) v2;
            if (a1.size() != a2.size()) {
                return false;
            }
            for (int i = 0; i < a1.size(); i++) {
                if (!isJsonValueEqual(normalizeValue(a1.get(i)), normalizeValue(a2.get(i)))) {
                    return false;
                }
            }
            return true;
        }
        return ObjectUtil.equal(v1, v2);
    }
}
