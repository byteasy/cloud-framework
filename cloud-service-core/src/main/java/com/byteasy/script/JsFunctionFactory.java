package com.byteasy.script;

import cn.hutool.core.codec.Base64;
import cn.hutool.extra.spring.SpringUtil;
import com.byteasy.Constant;
import com.byteasy.common.token.TokenUtil;
import com.byteasy.common.token.UserInfo;
import com.byteasy.common.token.UserInfoBuilder;
import com.byteasy.common.util.JacksonUtil;
import com.byteasy.common.util.OkHttpUtil;
import com.byteasy.component.ServiceInstanceUtil;
import com.byteasy.dto.ScriptFunction;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.byteasy.util.RandomUtil.random;

@Component
@Log4j2
public class JsFunctionFactory {

    private Map<String, ScriptFunction> jsFunctionMap = new HashMap<>();

    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void init() {
        Map<String, JsFunction> map = SpringUtil.getBeansOfType(JsFunction.class);
        for (Map.Entry<String, JsFunction> entry : map.entrySet()) {
            log.info("JsFunction name: {}, description: {}", entry.getValue().name(), entry.getValue().description());
            JsFunction jsFunction = entry.getValue();
            ScriptFunction scriptFunction = new ScriptFunction();
            scriptFunction.setServiceCode(jsFunction.serviceName());
            scriptFunction.setGroupName(jsFunction.group());
            scriptFunction.setName(jsFunction.name());
            scriptFunction.setDescription(jsFunction.description());
            scriptFunction.setFunction((args) -> {
                try {
                    return entry.getValue().run(args);
                } catch (Exception e) {
                    log.error(entry.getValue().name() + " run err", e);
                }
                return null;
            });
            jsFunctionMap.put(entry.getValue().name(), scriptFunction);
        }

        Map<String, JsFunctions> functions = SpringUtil.getBeansOfType(JsFunctions.class);
        for (Map.Entry<String, JsFunctions> functionsEntry : functions.entrySet()) {
            JsFunctions jsFunctions = functionsEntry.getValue();
            for (JsFunction jsFunction : jsFunctions.jsFunctions()) {
                log.info("JsFunction name: {}, description: {}", jsFunction.name(), jsFunction.description());
                ScriptFunction scriptFunction = new ScriptFunction();
                scriptFunction.setServiceCode(jsFunction.serviceName());
                scriptFunction.setGroupName(jsFunction.group());
                scriptFunction.setName(jsFunction.name());
                scriptFunction.setDescription(jsFunction.description());
                scriptFunction.setFunction((args) -> {
                    try {
                        return jsFunction.run(args);
                    } catch (Exception e) {
                        log.error(jsFunction.name() + " run err", e);
                    }
                    return null;
                });
                jsFunctionMap.put(jsFunction.name(), scriptFunction);
            }
        }
    }

    public Map<String, ScriptFunction> getJsFunctionMap() {
        return jsFunctionMap;
    }

    public Collection<ScriptFunction> getLocalFunctions() {
        return jsFunctionMap.values();
    }

    public void addRemoteFunction(Collection<ScriptFunction> scriptFunctions) {
        for (ScriptFunction scriptFunction : scriptFunctions) {
            if (jsFunctionMap.containsKey(scriptFunction.getName())) {
                continue;
            }
            log.info("remote JsFunction service: {} name: {}, group: {}, description: {}", scriptFunction.getServiceCode(), scriptFunction.getName(), scriptFunction.getGroupName(), scriptFunction.getDescription());
            scriptFunction.setFunction((args) -> {
                try {
                    DiscoveryClient discoveryClient = SpringUtil.getBean(DiscoveryClient.class);
                    ServiceInstanceUtil serviceInstanceUtil = SpringUtil.getBean(ServiceInstanceUtil.class);

                    List<ServiceInstance> instances = discoveryClient.getInstances(scriptFunction.getServiceCode());
                    Map<String, String> header = new HashMap<>();
                    UserInfo token = TokenUtil.getUserinfo();
                    if (token != null) {
                        header.put(Constant.USER_INFO, Base64.encode(JacksonUtil.toJson(token)));
                    } else {
                        header.put(Constant.USER_INFO, UserInfoBuilder.builderByMicroService(SpringUtil.getApplicationName(), SpringUtil.getApplicationName()));
                    }
                    ServiceInstance randomService = random(instances);
                    try {
                        String result = OkHttpUtil.postJson(randomService.getUri().toString() + serviceInstanceUtil.getServiceInstance(scriptFunction.getServiceCode()).getContextPath() + Constant.SAPI + "/scriptFunctionRunner/run/" + scriptFunction.getName(), JacksonUtil.toJson(args, true), header, null);
                        return JacksonUtil.parseObj(result);
                    } catch (Exception e) {
                        log.error(scriptFunction.getName() + " remote call err", e);
                        return null;
                    }
                } catch (Exception e) {
                    log.error(scriptFunction.getName() + " run err", e);
                }
                return null;
            });
            jsFunctionMap.put(scriptFunction.getName(), scriptFunction);
        }
    }
}
