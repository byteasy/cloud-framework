package com.byteasy.platform.service;

import cn.hutool.core.convert.Convert;
import com.byteasy.platform.entity.DictionaryItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DictionaryUtil {

    @Autowired
    private IDictionaryService dictionaryService;

    public String getItemValueByCode(String code, String itemCode) {
        DictionaryItem dictionaryItem = dictionaryService.selectByCodeAndItemCode(code, itemCode);
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
}
