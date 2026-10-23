package com.byteasy.platform.api;

import com.byteasy.api.IBaseEntityService;
import com.byteasy.platform.Constant;
import com.byteasy.platform.entity.ServiceInstance;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(Constant.SAPI + "/serviceInstances")
public interface ServiceInstanceClient extends IBaseEntityService<Long, ServiceInstance> {

}
