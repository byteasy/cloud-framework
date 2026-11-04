package com.byteasy.script;

import cn.hutool.extra.spring.SpringUtil;
import com.byteasy.Constant;
import com.byteasy.event.IPlatformReady;
import com.byteasy.exception.BizException;
import com.byteasy.platform.api.ScriptFunctionClient;
import com.byteasy.platform.dto.ScriptFunctionDTO;
import com.byteasy.util.JacksonUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class JsFunctionFactory implements IPlatformReady {

    private Map<String, ScriptFunctionDTO> jsFunctionMap = new HashMap<>();

    @Autowired
    private ScriptFunctionClient scriptFunctionService;
    @Autowired
    private RestClient.Builder restClientBuilder;

    @PostConstruct
    public void init() {

        Map<String, JsFunction> map = SpringUtil.getBeansOfType(JsFunction.class);
        for (Map.Entry<String, JsFunction> entry : map.entrySet()) {
            log.info("JsFunction name: {}, description: {}", entry.getValue().name(), entry.getValue().description());
            JsFunction jsFunction = entry.getValue();
            ScriptFunctionDTO scriptFunction = new ScriptFunctionDTO();
            scriptFunction.setServiceCode(jsFunction.serviceName());
            scriptFunction.setGroupName(jsFunction.group());
            scriptFunction.setName(jsFunction.name());
            scriptFunction.setDescription(jsFunction.description());
            scriptFunction.setFunction((args) -> {
                try {
                    return entry.getValue().run(args);
                } catch (Exception e) {
                    log.error(entry.getValue().name() + " run err", e);
                    throw new BizException(e.getMessage());
                }
            });
            jsFunctionMap.put(entry.getValue().name(), scriptFunction);
        }

        Map<String, JsFunctions> functions = SpringUtil.getBeansOfType(JsFunctions.class);
        for (Map.Entry<String, JsFunctions> functionsEntry : functions.entrySet()) {
            JsFunctions jsFunctions = functionsEntry.getValue();
            for (JsFunction jsFunction : jsFunctions.jsFunctions()) {
                log.info("JsFunction name: {}, description: {}", jsFunction.name(), jsFunction.description());
                ScriptFunctionDTO scriptFunction = new ScriptFunctionDTO();
                scriptFunction.setServiceCode(jsFunction.serviceName());
                scriptFunction.setGroupName(jsFunction.group());
                scriptFunction.setName(jsFunction.name());
                scriptFunction.setDescription(jsFunction.description());
                scriptFunction.setFunction((args) -> {
                    try {
                        return jsFunction.run(args);
                    } catch (Exception e) {
                        log.error(jsFunction.name() + " run err", e);
                        throw new BizException(e.getMessage());
                    }
                });
                jsFunctionMap.put(jsFunction.name(), scriptFunction);
            }
        }
    }

    public Map<String, ScriptFunctionDTO> getJsFunctionMap() {
        return jsFunctionMap;
    }

    public Collection<ScriptFunctionDTO> getLocalFunctions() {
        return jsFunctionMap.values();
    }

    public void addRemoteFunction(Collection<ScriptFunctionDTO> scriptFunctions) {
        for (ScriptFunctionDTO scriptFunction : scriptFunctions) {
            if (jsFunctionMap.containsKey(scriptFunction.getName())) {
                continue;
            }
            log.info("remote JsFunction service: {} name: {}, group: {}, description: {}", scriptFunction.getServiceCode(), scriptFunction.getName(), scriptFunction.getGroupName(), scriptFunction.getDescription());
            scriptFunction.setFunction((args) -> {
                try {
                    try {
                        String result = restClientBuilder.build().post()
                                .uri("http://" + scriptFunction.getServiceCode() + Constant.SAPI + "/inner/scriptFunctionRunner/run/" + scriptFunction.getName())
                                .body(JacksonUtil.toJson(args, true))
                                .contentType(MediaType.APPLICATION_JSON)
                                .retrieve().body(String.class);
                        return JacksonUtil.parseObj(result);
                    } catch (Exception e) {
                        log.error(scriptFunction.getName() + " remote call err", e);
                        throw new BizException(e.getMessage());
                    }
                } catch (Exception e1) {
                    log.error(scriptFunction.getName() + " run err", e1);
                    throw new BizException(e1.getMessage());
                }
            });
            jsFunctionMap.put(scriptFunction.getName(), scriptFunction);
        }
    }

    @Override
    public void ready() {
        log.info("JsFunctionFactory platformReady");

        try {
            Collection<ScriptFunctionDTO> scriptFunctions = getLocalFunctions();
            Collection<ScriptFunctionDTO> allFunctions = scriptFunctionService.register(scriptFunctions);
            addRemoteFunction(allFunctions);
        } catch (Exception e) {
            log.error("registerJsFunctionToPlatform err", e);
        }
    }

}
