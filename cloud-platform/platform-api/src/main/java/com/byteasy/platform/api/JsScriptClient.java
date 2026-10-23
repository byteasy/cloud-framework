package com.byteasy.platform.api;

import com.byteasy.api.IBaseEntityService;
import com.byteasy.platform.Constant;
import com.byteasy.platform.entity.JsScript;
import com.byteasy.platform.entity.OpenapiLog;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(Constant.SAPI + "/jsScripts")
public interface JsScriptClient extends IBaseEntityService<Long, JsScript> {

}
