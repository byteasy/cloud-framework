package com.byteasy.platform.service.impl;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.byteasy.platform.service.INacosConfigService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;

@Service
public class NacosConfigServiceImpl implements INacosConfigService {
    @Autowired
    private NacosConfigManager nacosConfigManager;
    @Resource
    private DiscoveryClient discoveryClient;
    @Resource
    private Environment env;

    @Override
    public List<String> getAllServices() {
        return discoveryClient.getServices();
    }

    @Override
    public String getContextPath(String serviceName) throws NacosException {
        ConfigService configService = nacosConfigManager.getConfigService();
        String content = configService.getConfig(serviceName + "-" + env.getActiveProfiles()[0] + ".yml", "DEFAULT_GROUP", 3000L);
        Yaml yaml = new Yaml();
        Map<String, Object> root = yaml.load(content);
        if (root == null) return "";

        try {
            Map<String, Object> server = (Map<String, Object>) root.get("server");
            Map<String, Object> servlet = (Map<String, Object>) server.get("servlet");
            return (String) servlet.get("context-path");
        } catch (Exception e) {
            return "";
        }
    }
}
