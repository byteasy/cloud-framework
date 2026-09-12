package com.byteasy.platform.oauth2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byteasy.platform.oauth2.entity.Oauth2RegisteredClient;
import com.byteasy.platform.oauth2.mapper.Oauth2RegisteredClientMapper;
import com.byteasy.platform.oauth2.service.IOauth2RegisteredClientService;
import com.byteasy.platform.oauth2.service.RegisteredClientConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>
 * OAuth2 已注册客户端信息表 服务实现类
 * </p>
 *
 * @author devops
 * @since 2026-02-22
 */
@Service
public class Oauth2RegisteredClientServiceImpl extends ServiceImpl<Oauth2RegisteredClientMapper, Oauth2RegisteredClient> implements IOauth2RegisteredClientService, RegisteredClientRepository {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void save(RegisteredClient registeredClient) {
        Oauth2RegisteredClient oauth2RegisteredClient = RegisteredClientConverter.toEntity(registeredClient);
        saveOrUpdate(oauth2RegisteredClient);
    }

    @Override
    public RegisteredClient findById(String id) {
        Oauth2RegisteredClient oauth2RegisteredClient = this.getById(id);
        return RegisteredClientConverter.toDomain(oauth2RegisteredClient);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        LambdaQueryWrapper<Oauth2RegisteredClient> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Oauth2RegisteredClient::getClientId, clientId);
        Optional<Oauth2RegisteredClient> oauth2RegisteredClient = getOneOpt(queryWrapper);
        return oauth2RegisteredClient.map(RegisteredClientConverter::toDomain).orElse(null);
    }


    @Override
    public boolean saveOrUpdate(Oauth2RegisteredClient entity) {
        if (!entity.getClientSecret().startsWith("{bcrypt}")) {
            entity.setClientSecret(passwordEncoder.encode(entity.getClientSecret()));
        }
        return super.saveOrUpdate(entity);
    }
}
