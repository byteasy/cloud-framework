package com.byteasy.script.controller;

import com.byteasy.Constant;
import com.byteasy.annotations.Openapi;
import com.byteasy.common.service.ICommonService;
import com.byteasy.dto.MethodInvokeDTO;
import com.byteasy.platform.dto.ScriptFunctionDTO;
import com.byteasy.script.JsFunctionFactory;
import com.byteasy.util.JacksonUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;

@RestController
@RequestMapping(Constant.SAPI + "/inner")
@Log4j2
public class ServiceInnerController {

    @Autowired
    private JsFunctionFactory factory;

    @Autowired
    @Qualifier("localCommonServiceImpl")
    private ICommonService localCommonService;

    @PostMapping("/scriptFunctionRunner/run/{name}")
    @Openapi
    public Object run(@PathVariable String name, @RequestBody(required = false) String argsString) {
        Object[] args = JacksonUtil.parseObjArray(argsString);
        ScriptFunctionDTO scriptFunction = factory.getJsFunctionMap().get(name);
        if (scriptFunction == null) {
            log.error("function name: {} not found", name);
            return null;
        }
        try {
            Object result = scriptFunction.getFunction().apply(args);
            return JacksonUtil.toJson(result, true);
        } catch (Exception e) {
            log.error("function name: {} execute error", name, e);
        }
        return null;
    }


    @PostMapping("/service/invoke")
    @Openapi
    public Object invoke(@RequestBody(required = false) String json) throws Exception {
        MethodInvokeDTO dto = (MethodInvokeDTO) JacksonUtil.parseObj(json);
        log.info("收到跨服务 ICommonService 调用: method={}, className={}", dto.getMethodName(), dto.getClassName());

        Object[] args = dto.getArguments() != null ? dto.getArguments().toArray() : new Object[0];

        // 匹配 ICommonService 的目标方法并执行
        for (Method method : ICommonService.class.getMethods()) {
            if (method.getName().equals(dto.getMethodName()) && method.getParameterCount() == args.length) {
                // 进行反射调用
                return JacksonUtil.toJson(method.invoke(localCommonService, args), true);
            }
        }
        throw new NoSuchMethodException("未找到匹配的方法: " + dto.getMethodName());
    }
}
