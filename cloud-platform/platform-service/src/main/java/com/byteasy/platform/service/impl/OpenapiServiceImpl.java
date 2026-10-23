package com.byteasy.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.byteasy.platform.entity.Openapi;
import com.byteasy.platform.mapper.OpenapiMapper;
import com.byteasy.platform.service.IOpenapiService;
import com.byteasy.util.RequestMappingResolver;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 平台接口定义 服务实现类
 * </p>
 *
 * @author devops
 * @since 2026-09-11
 */
@Service
public class OpenapiServiceImpl extends ServiceImpl<OpenapiMapper, Openapi> implements IOpenapiService {


    @Override
    public Openapi getByUriAndMethod(String actualUri, String uri, Openapi.HttpMethod method) {
        LambdaQueryWrapper<Openapi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Openapi::getUri, actualUri);
        queryWrapper.eq(Openapi::getHttpMethod, method);
        queryWrapper.last("limit 1");

        Openapi openapi = getOne(queryWrapper, false);
        if (openapi != null) {
            return openapi;
        }
        String[] segments = RequestMappingResolver.splitByPathVariable(uri);
        LambdaQueryWrapper<Openapi> queryWrapper2 = new LambdaQueryWrapper<>();
        for (String segment : segments) {
            queryWrapper2.and(s -> s.like(Openapi::getUri, segment));
        }
        queryWrapper2.eq(Openapi::getHttpMethod, method);
        openapi = getOne(queryWrapper2, false);
        return openapi;
    }

    @Override
    public Openapi getByCode(String code) {
        LambdaQueryWrapper<Openapi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Openapi::getCode, code);
        return getOne(queryWrapper, false);
    }
}
