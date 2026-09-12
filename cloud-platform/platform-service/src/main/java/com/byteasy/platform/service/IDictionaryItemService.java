package com.byteasy.platform.service;

import com.byteasy.platform.entity.DictionaryItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 字典项目定义表 服务类
 * </p>
 *
 * @author devops
 * @since 2026-03-24
 */
public interface IDictionaryItemService extends IService<DictionaryItem> {

    List<DictionaryItem> findByDictionaryId(Long dictionaryId);

    DictionaryItem findByDictionaryIdAndItemCode(Long dictionaryId, String itemCode);
}
