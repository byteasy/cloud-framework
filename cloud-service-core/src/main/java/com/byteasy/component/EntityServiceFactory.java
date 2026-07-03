package com.byteasy.component;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.api.IEntityDefineService;
import com.byteasy.common.token.TokenUtil;
import com.byteasy.common.token.UserInfo;
import com.byteasy.common.token.UserInfoBuilder;
import com.byteasy.common.util.CastUtil;
import com.byteasy.common.util.JacksonUtil;
import com.byteasy.common.util.OkHttpUtil;
import com.byteasy.dto.Column;
import com.byteasy.dto.EntityDefineDTO;
import com.byteasy.dto.FormatMap;
import com.byteasy.entity.Entity;
import com.byteasy.enums.RelationType;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Log4j2
public class EntityServiceFactory implements ApplicationContextAware {
    private static Map<Class<?>, ServiceImpl> entityServiceMap = new HashMap<Class<?>, ServiceImpl>();

    @Autowired
    private IEntityDefineService entityDefineService;
    @Autowired
    private SchemaDefineUtil schemaDefineUtil;
    @Resource
    private DiscoveryClient discoveryClient;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, ServiceImpl> map = applicationContext.getBeansOfType(ServiceImpl.class);
        for (ServiceImpl value : map.values()) {
            entityServiceMap.put(value.getEntityClass(), value);
            log.info("entity class: {}, service: {}", value.getEntityClass().getName(), value.getClass().getName());
        }
    }

    public static ServiceImpl getService(Class<?> entityClass) {
        ServiceImpl service = entityServiceMap.get(entityClass);
        if (service == null) {
            for (Map.Entry<Class<?>, ServiceImpl> entry : entityServiceMap.entrySet()) {
                if (entry.getKey().isAssignableFrom(entityClass)) {
                    return entry.getValue();
                }
            }
        }
        return service;
    }

    public Collection<ServiceImpl> getEntityService() {
        return entityServiceMap.values();
    }

    public void updateExternalEntity(Set<Column> externalKeys, List entities) {
        if (entities.isEmpty()) {
            return;
        }
        for (Column externalKey : externalKeys) {
            try {
                String className = externalKey.getExternalClass();
                EntityDefineDTO entityDefineDTO = entityDefineService.getEntityDefine(className);
                if (RelationType.EXTERNAL_KEY.equals(externalKey.getRelationType())) {
                    // 改为远程接口调用，方便递归填充参照对象
                    String serviceName = SpringUtil.getApplicationName();
                    if (serviceName.equalsIgnoreCase(entityDefineDTO.getServiceCode())) {
                        // 自身的领域实体，可直接从service调用
                        Set keys = getPrimaryKeys(externalKey.getName(), entities);
                        if (!CollectionUtil.isEmpty(keys)) {
                            QueryWrapper queryWrapper = new QueryWrapper();
                            queryWrapper.in(externalKey.getExternalClassPrimaryField(), keys);
                            Class<?> cls = Class.forName(className);
                            ServiceImpl service = getService(cls);
                            List objList = service.list(queryWrapper);
                            Map map = CastUtil.listToMap(objList, o -> ReflectUtil.getFieldValue(o, externalKey.getExternalClassPrimaryField()));

                            String fieldName = getClassFieldName(cls);
                            for (Object entity : entities) {
                                if (entity instanceof Entity<?>) {
                                    Entity e = (Entity) entity;
                                    e.setExtendField(fieldName, map.get(ReflectUtil.getFieldValue(e, externalKey.getName())));
                                } else if (entity instanceof FormatMap) {
                                    ((FormatMap) entity).put(fieldName, map.get(((FormatMap) entity).get(externalKey.getName())));
                                }
                            }
                            // 判断是否需要递归调用
                            /**
                             * 将外键关联的对象塞入拓展字段中
                             */
                            List<Column> columns = schemaDefineUtil.getColumns(className, false);
                            Set<Column> fieldExternalKeys = columns.stream().filter(f -> f.getExternalClass() != null).collect(Collectors.toSet());
                            if (!fieldExternalKeys.isEmpty() && !objList.isEmpty()) {
                                updateExternalEntity(fieldExternalKeys, objList);
                            }
                        }
                    } else {
                        String url = ((Map) entityDefineDTO.getExtendField().get("serviceInstance")).get("contextPath").toString() + entityDefineDTO.getUrl();
                        List<ServiceInstance> instances = discoveryClient.getInstances(entityDefineDTO.getServiceCode());
                        if (instances.isEmpty()) {
                            log.error("updateExternalEntity err, because no instances for service {}", entityDefineDTO.getServiceCode());
                            return;
                        }
                        Map<String, String> header = new HashMap<>();
                        List<Pair<String, String>> params = Lists.newArrayList();
                        UserInfo token = TokenUtil.getUserinfo();
                        if (token != null) {
                            header.put(Constant.USER_INFO, Base64.encode(JacksonUtil.toJson(token)));
                        } else {
                            header.put(Constant.USER_INFO, UserInfoBuilder.builderByMicroService(SpringUtil.getApplicationName(), SpringUtil.getApplicationName()));
                        }
                        Set keys = getPrimaryKeys(externalKey.getName(), entities);
                        for (Object key : keys) {
                            params.add(Pair.of(externalKey.getExternalClassPrimaryField() + "[]", key.toString()));
                        }
                        params.add(Pair.of(externalKey.getExternalClassPrimaryField() + "QueryType", "in"));
                        params.add(Pair.of("current", "1"));
                        params.add(Pair.of("size", "5000"));

                        ServiceInstance randomService = random(instances);
                        Result<Page<JSONObject>> result = OkHttpUtil.get((randomService.isSecure() ? "https://" : "http://") + randomService.getHost() + ":" + randomService.getPort() + url, header, params, null, new TypeReference<Result<Page<JSONObject>>>() {
                        });
                        Map map = CastUtil.listToMap(result.getData().getRecords(), o -> o.get(externalKey.getExternalClassPrimaryField()));

                        String fieldName = getClassFieldName(className);
                        for (Object entity : entities) {
                            if (entity instanceof Entity<?>) {
                                Entity e = (Entity) entity;
                                e.setExtendField(fieldName, map.get(ReflectUtil.getFieldValue(e, externalKey.getName())));
                            } else if (entity instanceof FormatMap) {
                                ((FormatMap) entity).put(fieldName, map.get(((FormatMap) entity).get(externalKey.getName())));
                            }
                        }
                    }

                } else if (RelationType.DETAIL_TABLE.equals(externalKey.getRelationType())) {
                    for (Object entity : entities) {
                        if (entity instanceof Entity<?>) {
                            Entity e = (Entity) entity;
                            // 将明细表的表头名称设置到对象属性里，前端展示后弹出明细表
                            ReflectUtil.setFieldValue(e, externalKey.getName(), entityDefineDTO.getName());
                        } else if (entity instanceof FormatMap) {
                            ((FormatMap) entity).put(externalKey.getName(), entityDefineDTO.getName());
                        }
                    }
                }
            } catch (Exception e) {
                log.error("entity class not found", e);
            }
        }
    }

    private static Set getPrimaryKeys(String fieldName, List entities) {
        Set set = new HashSet();
        for (Object entity : entities) {
            Object value = entity instanceof FormatMap ? ((FormatMap) entity).get(fieldName) : ReflectUtil.getFieldValue(entity, fieldName);
            set.add(value);
        }
        return set;
    }

    private static String getClassFieldName(Class<?> cls) {
        String simpleName = cls.getSimpleName();
        return simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
    }

    private static String getClassFieldName(String cls) {
        String[] arrs = cls.split("\\.");
        String simpleName = arrs[arrs.length - 1];
        return simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
    }

    private static ServiceInstance random(List<ServiceInstance> list) {
        if (list.size() == 1) {
            return list.get(0);
        }
        int random = RandomUtil.randomInt(list.size());
        return list.get(random);
    }
}
