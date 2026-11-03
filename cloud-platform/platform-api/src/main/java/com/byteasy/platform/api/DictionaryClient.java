package com.byteasy.platform.api;

import com.byteasy.Result;
import com.byteasy.api.IBaseEntityService;
import com.byteasy.platform.Constant;
import com.byteasy.platform.entity.Dictionary;
import com.byteasy.platform.entity.DictionaryItem;
import com.byteasy.platform.vo.DictionaryVO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(Constant.SAPI + "/dictionarys")
public interface DictionaryClient extends IBaseEntityService<Long, Dictionary> {

    @GetExchange("/codes/{code}")
    Result<DictionaryVO> getByCode(@PathVariable("code") String code);

    @GetExchange("/codes/{code}/items/{itemCode}")
    Result<DictionaryItem> getItemByCode(@PathVariable("code") String code, @PathVariable("itemCode") String itemCode);
}
