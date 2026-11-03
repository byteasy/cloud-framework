package com.byteasy.platform.api;

import com.byteasy.api.IBaseEntityService;
import com.byteasy.platform.Constant;
import com.byteasy.platform.entity.DictionaryItem;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(Constant.SAPI + "/dictionaryItems")
public interface DictionaryItemClient extends IBaseEntityService<Long, DictionaryItem> {

}
