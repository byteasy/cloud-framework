package com.byteasy.platform.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byteasy.enums.Enable;
import com.byteasy.platform.entity.DictionaryItem;
import com.byteasy.platform.mapper.DictionaryItemMapper;
import com.byteasy.platform.service.IDictionaryItemService;
import com.byteasy.platform.service.IDictionaryService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 字典项目定义表 服务实现类
 * </p>
 *
 * @author devops
 * @since 2026-03-24
 */
@Service
public class DictionaryItemServiceImpl extends ServiceImpl<DictionaryItemMapper, DictionaryItem> implements IDictionaryItemService {

    @Override
    public List<DictionaryItem> findByDictionaryId(Long dictionaryId) {
        LambdaQueryWrapper<DictionaryItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictionaryItem::getDictionaryId, dictionaryId);
        queryWrapper.eq(DictionaryItem::getStatus, Enable.ENABLE);
        queryWrapper.orderByAsc(DictionaryItem::getShowOrder);
        return list(queryWrapper);
    }

    @Override
    public DictionaryItem findByDictionaryIdAndItemCode(Long dictionaryId, String itemCode) {
        LambdaQueryWrapper<DictionaryItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictionaryItem::getDictionaryId, dictionaryId);
        queryWrapper.eq(DictionaryItem::getStatus, Enable.ENABLE);
        queryWrapper.eq(DictionaryItem::getCode, itemCode);
        queryWrapper.orderByAsc(DictionaryItem::getShowOrder);
        return getOne(queryWrapper, false);
    }

    @Override
    public boolean removeById(Serializable id, boolean useFill) {
        SpringUtil.getBean(IDictionaryService.class).invalidateCache();
        return super.removeById(id, useFill);
    }

    @Override
    public boolean saveOrUpdate(DictionaryItem entity) {
        SpringUtil.getBean(IDictionaryService.class).invalidateCache();
        return super.saveOrUpdate(entity);
    }

    @Override
    public boolean updateById(DictionaryItem entity) {
        SpringUtil.getBean(IDictionaryService.class).invalidateCache();
        return super.updateById(entity);
    }
}
