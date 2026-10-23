package com.byteasy.platform.api;

import com.byteasy.api.IBaseEntityService;
import com.byteasy.platform.Constant;
import com.byteasy.platform.entity.OpenapiLog;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(Constant.SAPI + "/openapiLogs")
public interface OpenapiLogClient extends IBaseEntityService<Long, OpenapiLog> {

}
