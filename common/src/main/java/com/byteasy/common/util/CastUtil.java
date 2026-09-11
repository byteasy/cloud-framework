package com.byteasy.common.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CastUtil {

    public static <K, T> Map<K, T> listToMap(List<T> entities, Function<T, K> function) {
        if (entities == null) {
            return null;
        }
        Map<K, T> map = Maps.newHashMap();
        for (T entity : entities) {
            map.put(function.apply(entity), entity);
        }
        return map;
    }

    public static <K, T> Map<K, T> collectionToMap(Collection<T> entities, Function<T, K> function) {
        if (entities == null) {
            return null;
        }
        Map<K, T> map = Maps.newHashMap();
        for (T entity : entities) {
            map.put(function.apply(entity), entity);
        }
        return map;
    }

    public static <K, T, V> Map<K, V> collectionToMap(Collection<T> entities, Function<T, K> keyFunction, Function<T, V> valueFunction) {
        if (entities == null) {
            return null;
        }
        Map<K, V> map = Maps.newHashMap();
        for (T entity : entities) {
            map.put(keyFunction.apply(entity), valueFunction.apply(entity));
        }
        return map;
    }

    public static <K, T, V> Map<K, V> listToMap(List<T> entities, Function<T, K> keyFunction, Function<T, V> valueFunction) {
        if (entities == null) {
            return null;
        }
        Map<K, V> map = Maps.newHashMap();
        for (T entity : entities) {
            map.put(keyFunction.apply(entity), valueFunction.apply(entity));
        }
        return map;
    }

    public static <T, V> Set<V> listToSet(List<T> entities, Function<T, V> function) {
        if (entities == null) {
            return null;
        }
        return entities.stream().map(function).collect(Collectors.toSet());
    }

    public static <T, V> List<V> objectListToColumnList(List<T> entities, Function<T, V> function) {
        if (entities == null) {
            return null;
        }
        return entities.stream().map(function).collect(Collectors.toList());
    }

    public static <T> List<T> copyList(List<?> list, Class<T> cls) {
        return copyList(list, cls, null);
    }

    public static <T> List<T> copyList(List<?> list, Class<T> cls, Consumer<T> consumer) {
        if (list == null) {
            return null;
        }
        List<T> resultList = Lists.newArrayList();
        for (Object o : list) {
            T target = ReflectUtil.newInstance(cls);
            BeanUtil.copyProperties(o, target);
            if (consumer != null) {
                consumer.accept(target);
            }
            resultList.add(target);
        }
        return resultList;
    }

    public static List<Map<String, Object>> beanListToMapList(Collection<?> list) {
        if (list == null) {
            return null;
        }
        List<Map<String, Object>> resultList = Lists.newArrayList();
        for (Object o : list) {
            resultList.add(BeanUtil.beanToMap(o));
        }
        return resultList;
    }

    public static <T> List<T> mapListToBeanList(List<Map<String, Object>> mapList, Class<T> cls) {
        if (mapList == null) return null;
        List<T> resultList = Lists.newArrayList();
        for (Map<String, Object> map : mapList) {
            T target = ReflectUtil.newInstance(cls);
            BeanUtil.fillBeanWithMap(map, target, true);
            resultList.add(target);
        }
        return resultList;
    }


    public static <T> T copyBean(Object src, Class<T> cls) {
        if (src == null) {
            return null;
        }
        T target = ReflectUtil.newInstance(cls);
        BeanUtil.copyProperties(src, target);
        return target;
    }

    public static <SRC, TARGET> Page<TARGET> copyPage(Page<SRC> page, Class<TARGET> cls) {
        if (page == null) {
            return null;
        }
        Page<TARGET> target = new Page<>();
        target.setCurrent(page.getCurrent());
        target.setSize(page.getSize());
        target.setTotal(page.getTotal());
        target.setRecords(copyList(page.getRecords(), cls));
        return target;
    }
}
