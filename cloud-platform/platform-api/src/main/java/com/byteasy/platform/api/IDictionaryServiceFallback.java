package com.byteasy.platform.api;

import com.byteasy.Result;
import com.byteasy.platform.entity.DictionaryItem;
import com.byteasy.platform.vo.DictionaryVO;

public class IDictionaryServiceFallback implements IDictionaryService {

    @Override
    public Result<DictionaryVO> getByCode(String code) {
        return Result.error();
    }

    @Override
    public Result<DictionaryItem> getItemByCode(String code, String itemCode) {
        return Result.error();
    }
}
