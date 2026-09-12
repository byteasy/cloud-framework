package com.byteasy.platform.service;

import com.alibaba.nacos.api.exception.NacosException;

import java.util.List;

public interface INacosConfigService {

    List<String> getAllServices();

    String getContextPath(String serviceName) throws NacosException;
}
