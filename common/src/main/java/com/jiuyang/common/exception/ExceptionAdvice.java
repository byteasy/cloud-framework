package com.jiuyang.common.exception;


import com.jiuyang.common.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionAdvice {
    private static final Logger logger = LogManager.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result<String> exceptionHandler(Exception e) {
        logger.error("exceptionHandler", e);
        return Result.error(e.getLocalizedMessage());
    }

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public Result<String> runtimeExceptionHandler(RuntimeException e) {
        logger.error("runtimeExceptionHandler", e);
        return Result.error(e.getLocalizedMessage());
    }
}
