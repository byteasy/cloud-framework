package com.byteasy.platform.service.impl;

import com.byteasy.platform.entity.ResourceOpenapi;
import com.byteasy.platform.mapper.ResourceOpenapiMapper;
import com.byteasy.platform.service.IResourceOpenapiService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * OAuth2客户端与平台资源的授权关联表 服务实现类
 * </p>
 *
 * @author devops
 * @since 2026-03-03
 */
@Service
public class ResourceOpenapiServiceImpl extends ServiceImpl<ResourceOpenapiMapper, ResourceOpenapi> implements IResourceOpenapiService {

}
