package com.byteasy.common.component;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.common.token.TokenUtil;
import com.byteasy.common.token.UserInfo;
import com.byteasy.common.token.UserInfoBuilder;
import com.byteasy.common.util.CastUtil;
import com.byteasy.common.util.FieldDefineUtil;
import com.byteasy.common.util.JacksonUtil2;
import com.byteasy.component.CacheFactory;
import com.byteasy.component.ICacheLoader;
import com.byteasy.dto.FormatMap;
import com.byteasy.entity.Entity;
import com.byteasy.enums.RelationType;
import com.byteasy.event.IPlatformReady;
import com.byteasy.platform.api.EntityDefineClient;
import com.byteasy.platform.entity.EntityDefine;
import com.byteasy.platform.entity.FieldDefine;
import com.byteasy.util.FieldValueUtil;
import com.byteasy.util.I18nUtil;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;

import java.util.*;

@Component
@Log4j2
public class EntityServiceFactory implements ApplicationContextAware, IPlatformReady {

    @Autowired
    private CacheFactory cacheFactory;
    @Autowired
    private EntityDefineLoadingCache entityDefineLoadingCache;
    @Resource
    private DiscoveryClient discoveryClient;
    @Autowired
    private EntityDefineClient entityDefineService;

    private static Map<Class<?>, ServiceImpl> entityServiceMap = Maps.newHashMap();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, ServiceImpl> map = applicationContext.getBeansOfType(ServiceImpl.class);
        for (ServiceImpl value : map.values()) {
            entityServiceMap.put(value.getEntityClass(), value);
            log.info("entity class: {}, service: {}", value.getEntityClass().getName(), value.getClass().getName());
        }
    }

    private void setExternalKeyByService(FieldDefine externalKey, ServiceImpl service, List entities) {
        Set keys = getPrimaryKeys(externalKey.getName(), entities);

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in(externalKey.getExternalClassPrimaryField(), keys);
        Map<String, Object> map = CastUtil.listToMap(service.list(wrapper), e -> String.valueOf(FieldValueUtil.getFieldValue(e, externalKey.getExternalClassPrimaryField())));
        entities.forEach(entity -> {
            Object key = FieldValueUtil.getFieldValue(entity, externalKey.getName());
            String fieldName = getClassFieldName(externalKey.getExternalClass());
            FieldValueUtil.setFieldValue(entity, fieldName, map.get(String.valueOf(key)));
        });
    }

    public void updateExternalEntity(Set<FieldDefine> externalKeys, List entities) {
        if (entities.isEmpty()) {
            return;
        }
        for (FieldDefine externalKey : externalKeys) {
            try {
                String className = externalKey.getExternalClass();
                /**
                 * 如果是同一个服务实例的领域对象，直接本地填充
                 */
                ServiceImpl service = getService(className);
                if (service != null) {
                    setExternalKeyByService(externalKey, service, entities);
                    continue;
                }

                EntityDefine entityDefine = entityDefineLoadingCache.load(className);
                if (entityDefine == null) {
                    log.error("entityDefineDTO is null, className: {}", className);
                    continue;
                }
                if (RelationType.EXTERNAL_KEY.equals(externalKey.getRelationType())) {
                    Map map;
                    Set keys = getPrimaryKeys(externalKey.getName(), entities);

                    if (cacheFactory.cacheable(className)) {
                        ICacheLoader loader = cacheFactory.getCacheLoader(className);
                        map = Maps.newHashMap();
                        for (Object key : keys) {
                            Object externalEntity = loader.load(key);
                            map.put(FieldValueUtil.getFieldValue(externalEntity, externalKey.getExternalClassPrimaryField()), externalEntity);
                        }
                    } else {
                        String url = ((Map) entityDefine.getExtendField().get("serviceInstance")).get("contextPath").toString() + Constant.SAPI + "/common/" + className + "/all";
                        List<ServiceInstance> instances = discoveryClient.getInstances(entityDefine.getServiceCode());
                        if (instances.isEmpty()) {
                            log.error("updateExternalEntity err, because no instances for service {}", entityDefine.getServiceCode());
                            return;
                        }

                        Map<String, String> header = new HashMap<>();
                        UserInfo token = TokenUtil.getUserinfo();
                        if (token != null) {
                            header.put(Constant.USER_INFO, Base64.encode(JacksonUtil2.toJson(token)));
                        } else {
                            header.put(Constant.USER_INFO, UserInfoBuilder.builderByMicroService(SpringUtil.getApplicationName(), SpringUtil.getApplicationName()));
                        }

                        List<Pair<String, String>> params = Lists.newArrayList();
                        for (Object key : keys) {
                            params.add(Pair.of(externalKey.getExternalClassPrimaryField() + "[]", key.toString()));
                        }
                        params.add(Pair.of(externalKey.getExternalClassPrimaryField() + "QueryType", "in"));

                        ServiceInstance randomService = random(instances);
                        Result<List<JSONObject>> result = OkHttpUtil.get((randomService.isSecure() ? "https://" : "http://") + randomService.getHost() + ":" + randomService.getPort() + url, header, params, null,
                                new TypeReference<Result<List<JSONObject>>>() {
                                });
                        map = CastUtil.listToMap(result.getData(), o -> FieldDefineUtil.toValue(o, externalKey.getType(), externalKey.getExternalClassPrimaryField()));
                    }

                    String fieldName = getClassFieldName(className);
                    for (Object entity : entities) {
                        if (entity instanceof Entity<?>) {
                            Entity e = (Entity) entity;
                            e.setExtendField(fieldName, map.get(ReflectUtil.getFieldValue(e, externalKey.getName())));
                        } else if (entity instanceof FormatMap) {
                            Object value = ((FormatMap) entity).get(externalKey.getName());
                            if (map.containsKey(value)) {
                                ((FormatMap) entity).put(fieldName, map.get(value));
                            } else if (map.containsKey(String.valueOf(value))) {
                                // ID等Long类型，会因为Jackson序列化变为字符串
                                ((FormatMap) entity).put(fieldName, map.get(String.valueOf(value)));
                            }
                        }
                    }
//                    }

                } else if (RelationType.DETAIL_TABLE.equals(externalKey.getRelationType())) {
                    for (Object entity : entities) {
                        if (entity instanceof Entity<?>) {
                            Entity e = (Entity) entity;
                            // 将明细表的表头名称设置到对象属性里，前端展示后弹出明细表
                            ReflectUtil.setFieldValue(e, externalKey.getName(), entityDefine.getName());
                        } else if (entity instanceof FormatMap) {
                            ((FormatMap) entity).put(externalKey.getName(), entityDefine.getName());
                        }
                    }
                }
            } catch (Exception e) {
                log.error(String.format("entity class not found, className: {} fieldName: {} external className: %s", externalKey.getClassName(), externalKey.getName(), externalKey.getExternalClass()), e);
            }
        }
    }

    private static Set getPrimaryKeys(String fieldName, List entities) {
        Set set = new HashSet();
        for (Object entity : entities) {
            Object value = FieldValueUtil.getFieldValue(entity, fieldName);
            if (value != null) {
                set.add(value);
            }
        }
        return set;
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

    public static ServiceImpl getService(String className) {
        try {
            Class<?> cls = Class.forName(className);
            if (entityServiceMap.containsKey(cls)) {
                return entityServiceMap.get(cls);
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public void ready() {
        log.info("EntityServiceFactory platformReady");
        try {
            List<EntityDefine> list = new ArrayList<>();
            for (ServiceImpl service : entityServiceMap.values()) {
                Class<?> entityClass = service.getEntityClass();

                EntityDefine entityDefineDTO = new EntityDefine();
                entityDefineDTO.setCode(entityClass.getName());
                entityDefineDTO.setTableName(FieldDefineUtil.getTableName(entityClass));
                String classDesc = FieldDefineUtil.getSchemaDescription(entityClass);
                String className = FieldDefineUtil.getSchemaName(entityClass);
                if (StringUtils.isEmpty(className)) {
                    className = entityClass.getSimpleName();
                }
                if (StringUtils.isEmpty(classDesc)) {
                    classDesc = entityClass.getSimpleName();
                }
                if (className.startsWith("{")) {
                    entityDefineDTO.setName(className);
                } else {
                    entityDefineDTO.setName(I18nUtil.convertI18nFieldZhValue(className));
                }
                entityDefineDTO.setRemark(classDesc);
                entityDefineDTO.setServiceCode(SpringUtil.getApplicationName());
                list.add(entityDefineDTO);
            }
            Result result = entityDefineService.saveOrUpdateBatch(list);
            log.info("ServiceInstanceAutoRegister registerEntityDefine result: {}", JacksonUtil2.toJson(result));
        } catch (Exception e) {
            log.error("ServiceInstanceAutoRegister err", e);
        }
    }

    @Override
    public int order() {
        return Integer.MIN_VALUE + 1;
    }
}
