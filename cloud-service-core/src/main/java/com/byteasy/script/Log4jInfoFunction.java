package com.byteasy.script;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class Log4jInfoFunction implements JsFunction {
    private Logger logger = LogManager.getLogger(Log4jInfoFunction.class);

    @Override
    public String name() {
        return "logInfo";
    }

    @Override
    public String description() {
        return "日志打印函数";
    }

    @Override
    public Object run(Object[] args) throws Exception {
        // TODO
        logger.info(args);
        return null;
    }
}
