package com.byteasy.platform.api;

import com.byteasy.platform.Constant;
import com.byteasy.platform.entity.DictionaryItem;
import com.byteasy.platform.vo.DictionaryVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "platform-service",
        contextId = "IDictionaryService",
        path = Constant.SAPI + "/dictionarys",
        fallback = IDictionaryServiceFallback.class)
public interface IDictionaryService {

    @GetMapping("/rpc/codes/{code}")
    DictionaryVO getByCode(@PathVariable("code") String code);

    @GetMapping("/rpc/codes/{code}/items/{itemCode}")
    DictionaryItem getItemByCode(@PathVariable("code") String code, @PathVariable("itemCode") String itemCode);
}
