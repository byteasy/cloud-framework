package com.byteasy.common.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.byteasy.common.component.EntityDefineLoadingCache;
import com.byteasy.common.component.RepositoryMutationAspect;
import com.byteasy.common.service.IAuditLogService;
import com.byteasy.common.service.impl.CommonServiceImpl;
import com.byteasy.common.token.TokenUtil;
import com.byteasy.common.token.UserInfo;
import com.byteasy.entity.AuditLog;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.IEntityChangeHandler;
import com.byteasy.platform.dto.CreateAuditLogTableDTO;
import com.byteasy.platform.entity.EntityDefine;
import com.byteasy.util.CastUtil;
import com.byteasy.util.JacksonUtil;
import com.google.common.collect.Lists;
import io.vertx.core.eventbus.EventBus;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@Log4j2
public class EntityChangeAuditRecordHandler implements IEntityChangeHandler {

    @Autowired
    private EntityDefineLoadingCache entityDefineLoadingCache;
    @Autowired
    private EventBus eventBus;
    @Autowired
    private IAuditLogService auditLogService;

    @Override
    public String type() {
        return EntityChangeAuditRecordHandler.class.getName();
    }

    @Override
    public void handle(String src, EntityChangeEvent event) {
        EntityDefine entityDefine = entityDefineLoadingCache.load(event.getEntityClass());
        if (entityDefine != null) {

            // 判断是否需要创建审计记录表
            if (StringUtils.isNotEmpty(entityDefine.getTableName())) {
                if (EntityDefine.class.getName().equals(event.getEntityClass())) {
                    // 监听实体定义表的变更,获取是否审计字段，如果新建的实体是“是”或者由不是改为是，触发审计表的创建
                    switch (event.getChangeType()) {
                        case INSERT -> {
                            if (CommonServiceImpl.class.getName().equals(src)) {
                                for (Object afterEntity : event.getAfterEntities()) {
                                    Boolean auditable = MapUtil.getBool((Map<?, ?>) afterEntity, "auditable");
                                    if (auditable != null && auditable) {
                                        sendCreateEntityAuditTable(CastUtil.mapToBean((Map<String, Object>) afterEntity, CreateAuditLogTableDTO.class));
                                    }
                                }
                            } else if (RepositoryMutationAspect.class.getName().equals(src)) {
                                for (Object afterEntity : event.getAfterEntities()) {
                                    Boolean auditable = ((EntityDefine) afterEntity).getAuditable();
                                    if (auditable != null && auditable) {
                                        sendCreateEntityAuditTable(new CreateAuditLogTableDTO((EntityDefine) afterEntity));
                                    }
                                }
                            }
                        }
                        case UPDATE -> {
                            if (CommonServiceImpl.class.getName().equals(src)) {
                                for (int i = 0; i < event.getAfterEntities().size(); i++) {
                                    Object afterEntity = event.getAfterEntities().get(i);
                                    Object beforeEntity = event.getBeforeEntities().get(i);
                                    Boolean auditableAfter = MapUtil.getBool((Map<?, ?>) afterEntity, "auditable");
                                    Boolean auditableBefore = MapUtil.getBool((Map<?, ?>) beforeEntity, "auditable");
                                    if (auditableBefore != null && auditableAfter != null && auditableBefore == false && auditableAfter == true) {
                                        sendCreateEntityAuditTable(CastUtil.mapToBean((Map<String, Object>) afterEntity, CreateAuditLogTableDTO.class));
                                    }
                                }
                            } else if (RepositoryMutationAspect.class.getName().equals(src)) {
                                for (int i = 0; i < event.getAfterEntities().size(); i++) {
                                    Object afterEntity = event.getAfterEntities().get(i);
                                    Object beforeEntity = event.getBeforeEntities().get(i);
                                    Boolean auditableAfter = ((EntityDefine) afterEntity).getAuditable();
                                    Boolean auditableBefore = ((EntityDefine) beforeEntity).getAuditable();
                                    if (auditableBefore != null && auditableAfter != null && auditableBefore == false && auditableAfter == true) {
                                        sendCreateEntityAuditTable(new CreateAuditLogTableDTO((EntityDefine) afterEntity));
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 记录每次实体的变化
            if (entityDefine.getAuditable() != null && entityDefine.getAuditable()) {
                switch (event.getChangeType()) {
                    case UPDATE, INSERT -> {
                        saveAuditLog(entityDefine, event.getChangeType(), event.getAfterEntities());
                    }
                    case DELETE -> {
                        saveAuditLog(entityDefine, event.getChangeType(), event.getBeforeEntities());
                    }
                }
            }
        } else {
            log.warn("Entity: {} is not define.", event.getEntityClass());
        }
    }

    private void sendCreateEntityAuditTable(CreateAuditLogTableDTO entityDefine) {
        eventBus.send(entityDefine.getServiceCode(), JacksonUtil.toJson(entityDefine, true));
    }

    private void saveAuditLog(EntityDefine entityDefine, EntityChangeEvent.ChangeType changeType, List list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        List<AuditLog> auditLogs = Lists.newArrayListWithCapacity(list.size());
        for (Object entity : list) {
            AuditLog auditLog = new AuditLog();
            auditLog.setChangeType(changeType);
            auditLog.setCreatedTime(LocalDateTime.now());
            UserInfo userInfo = TokenUtil.getUserinfo();

            // Job更新数据，拿不到userInfo
            if (userInfo != null) {
                auditLog.setCreatedBy(StringUtils.deleteWhitespace(userInfo.getName()));
                auditLog.setCreatorId(userInfo.getUserId());
            }
            auditLog.setEntityId(String.valueOf(BeanUtil.getFieldValue(entity, "id")));
            auditLog.setEntityContent(JacksonUtil.toJson(entity));
            auditLogs.add(auditLog);
        }

        auditLogService.batchInsert(entityDefine, auditLogs);
    }
}
