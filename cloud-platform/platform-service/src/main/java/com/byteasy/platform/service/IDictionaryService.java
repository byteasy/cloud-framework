package com.byteasy.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byteasy.platform.entity.Dictionary;
import com.byteasy.platform.entity.DictionaryItem;
import com.byteasy.platform.vo.DictionaryVO;

/**
 * <p>
 * 字典定义表 服务类
 * </p>
 *
 * @author devops
 * @since 2026-03-24
 */
public interface IDictionaryService extends IService<Dictionary> {

    DictionaryVO selectByCode(String code);

    DictionaryItem selectByCodeAndItemCode(String code, String itemCode);

    void invalidateCache();
}
