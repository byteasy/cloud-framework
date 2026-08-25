package com.byteasy.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteasy.Result;
import com.byteasy.dto.ServiceInstanceDTO;

import java.util.List;
import java.util.Map;

public class IServiceInstanceServiceFallback implements IServiceInstanceService {


    @Override
    public Result<ServiceInstanceDTO> saveOrUpdate(ServiceInstanceDTO serviceInstance) {
        return null;
    }

    @Override
    public Result<Page<ServiceInstanceDTO>> page(long current, long size) {
        return null;
    }

    @Override
    public Result<List<ServiceInstanceDTO>> list(Map<String, Object> queryParams) {
        return null;
    }
}
