package com.byteasy.platform.api;

import com.byteasy.platform.entity.DictionaryItem;
import com.byteasy.platform.vo.DictionaryVO;

public class IDictionaryServiceFallback implements IDictionaryService {

    @Override
    public DictionaryVO getByCode(String code) {
        return null;
    }

    @Override
    public DictionaryItem getItemByCode(String code, String itemCode) {
        return null;
    }
}
