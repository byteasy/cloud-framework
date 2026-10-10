package com.byteasy.platform.service;

import cn.hutool.core.convert.Convert;
import com.byteasy.enums.Enable;
import com.byteasy.platform.entity.Dictionary;
import com.byteasy.platform.entity.DictionaryItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DictionaryUtil {

    @Autowired
    private IDictionaryService dictionaryService;
    @Autowired
    private IDictionaryItemService dictionaryItemService;

    public DictionaryItem getItemByCode(String code, String itemCode) {
        return dictionaryService.selectByCodeAndItemCode(code, itemCode);
    }

    public String getItemValueByCode(String code, String itemCode) {
        DictionaryItem dictionaryItem = getItemByCode(code, itemCode);
        if (dictionaryItem == null) {
            return null;
        }
        return dictionaryItem.getValue();
    }

    public <T> T get(String code, String itemCode, Class<T> type, T defaultValue) {
        String value = getItemValueByCode(code, itemCode);
        return Convert.convertQuietly(type, value, defaultValue);
    }

    public int getInt(String code, String itemCode, int defaultValue) {
        return get(code, itemCode, int.class, defaultValue);
    }

    public long getLong(String code, String itemCode, long defaultValue) {
        return get(code, itemCode, Long.class, defaultValue);
    }

    public String getStr(String code, String itemCode, String defaultValue) {
        return get(code, itemCode, String.class, defaultValue);
    }

    @Transactional
    public void saveOrUpdate(String code, String itemCode, String itemValue) {
        DictionaryItem dictionaryItem = getItemByCode(code, itemCode);
        if (dictionaryItem == null) {
            Dictionary dictionary = dictionaryService.selectByCodeFromDb(code);
            if (dictionary == null) {
                dictionary = new Dictionary();
                dictionary.setCode(code);
                dictionary.setStatus(Enable.ENABLE);
                dictionary.setName(code);
                dictionaryService.save(dictionary);
            }
            dictionaryItem = new DictionaryItem();
            dictionaryItem.setCode(itemCode);
            dictionaryItem.setValue(itemValue);
            dictionaryItem.setLabel(itemValue);
            dictionaryItem.setStatus(Enable.ENABLE);
            dictionaryItem.setDictionaryId(dictionary.getId());
            dictionaryItemService.save(dictionaryItem);
        } else {
            dictionaryItem.setValue(itemValue);
            dictionaryItemService.updateById(dictionaryItem);
        }
    }
}
