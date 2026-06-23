package com.byteasy.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteasy.Result;
import com.byteasy.dto.ServiceInstanceDTO;

public class IServiceInstanceServiceFallback implements IServiceInstanceService {


    @Override
    public Result<ServiceInstanceDTO> saveOrUpdate(ServiceInstanceDTO serviceInstance) {
        return null;
    }

    @Override
    public Result<Page<ServiceInstanceDTO>> page(long current, long size) {
        return null;
    }
}
