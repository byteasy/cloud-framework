package com.byteasy.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.byteasy.common.util.CastUtil;
import com.byteasy.enums.Enable;
import com.byteasy.platform.entity.Dictionary;
import com.byteasy.platform.entity.DictionaryItem;
import com.byteasy.platform.mapper.DictionaryMapper;
import com.byteasy.platform.service.IDictionaryItemService;
import com.byteasy.platform.service.IDictionaryService;
import com.byteasy.platform.vo.DictionaryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.Duration;
import java.util.concurrent.ExecutionException;

/**
 * <p>
 * 字典定义表 服务实现类
 * </p>
 *
 * @author devops
 * @since 2026-03-24
 */
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements IDictionaryService {

    @Autowired
    private IDictionaryItemService dictionaryItemService;

    private LoadingCache<String, DictionaryVO> dictionaryCache = CacheBuilder.newBuilder().maximumSize(10000).expireAfterAccess(Duration.ofHours(12)).recordStats().build(new CacheLoader<String, DictionaryVO>() {
        @Override
        public DictionaryVO load(String key) throws Exception {
            return loadFromDbByCode(key);
        }
    });

    @Override
    public DictionaryVO selectByCode(String code) {
        try {
            return dictionaryCache.get(code);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    protected DictionaryVO loadFromDbByCode(String code) {
        LambdaQueryWrapper<Dictionary> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dictionary::getCode, code);
        queryWrapper.eq(Dictionary::getStatus, Enable.ENABLE);
        queryWrapper.last("limit 1");
        Dictionary dictionary = getOne(queryWrapper, false);
        if (dictionary == null) {
            return null;
        }

        DictionaryVO dictionaryVO = CastUtil.copyBean(dictionary, DictionaryVO.class);
        dictionaryVO.setItems(dictionaryItemService.findByDictionaryId(dictionary.getId()));
        return dictionaryVO;
    }

    @Override
    public DictionaryItem selectByCodeAndItemCode(String code, String itemCode) {
        DictionaryVO dictionaryVO = selectByCode(code);
        if (dictionaryVO == null) {
            return null;
        }

        if (dictionaryVO.getItems() == null) {
            return null;
        }

        for (DictionaryItem item : dictionaryVO.getItems()) {
            if (item.getCode().equals(itemCode)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public void invalidateCache() {
        dictionaryCache.invalidateAll();
    }

    @Override
    public boolean removeById(Serializable id, boolean useFill) {
        dictionaryCache.invalidateAll();
        return super.removeById(id, useFill);
    }

    @Override
    public boolean saveOrUpdate(Dictionary entity) {
        dictionaryCache.invalidate(entity.getCode());
        return super.saveOrUpdate(entity);
    }

    @Override
    public boolean updateById(Dictionary entity) {
        dictionaryCache.invalidate(entity.getCode());
        return super.updateById(entity);
    }
}
