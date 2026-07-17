package com.byteasy.script;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class Log4jErrFunction implements JsFunction {

    @Override
    public String name() {
        return "logErr";
    }

    @Override
    public String description() {
        return "日志打印函数";
    }

    @Override
    public Object run(Object[] args) throws Exception {
        // TODO
        log.error(args);
        return null;
    }
}
