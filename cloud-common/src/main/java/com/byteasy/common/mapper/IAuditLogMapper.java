package com.byteasy.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.byteasy.entity.AuditLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 字典项目定义表 Mapper 接口
 * </p>
 *
 * @author devops
 * @since 2026-03-24
 */
@Mapper
public interface IAuditLogMapper extends BaseMapper<AuditLog> {

    void batchInsert(@Param("tableName") String tableName,
                     @Param("auditLogs") Collection<AuditLog> auditLogs);

    List<AuditLog> queryForList(@Param("tableName") String tableName,
                                @Param("entityId") Long entityId,
                                @Param("start") LocalDateTime start,
                                @Param("end") LocalDateTime end,
                                @Param("current") long current,
                                @Param("pageSize") long pageSize);

    long countForPage(@Param("tableName") String tableName,
                      @Param("entityId") Long entityId,
                      @Param("start") LocalDateTime start,
                      @Param("end") LocalDateTime end);
}
