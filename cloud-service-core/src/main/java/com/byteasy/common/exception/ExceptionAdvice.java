package com.byteasy.common.exception;


import cn.hutool.core.exceptions.ExceptionUtil;
import com.byteasy.MetricConstant;
import com.byteasy.Result;
import com.byteasy.Status;
import com.byteasy.component.I18nUtil;
import com.byteasy.exception.BizException;
import com.byteasy.monitor.MonitorFacade;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.apache.catalina.util.FilterUtil.getRequestPath;

@ControllerAdvice
public class ExceptionAdvice {

    private static final Logger logger = LogManager.getLogger(ExceptionAdvice.class);
    @Autowired
    private MonitorFacade monitorFacade;

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result<String> exceptionHandler(Exception e) {
        logger.error("exceptionHandler", e);
        doErrorMetric();
        return Result.of(Status.DUPLICATE_KEY_EXCEPTION, ExceptionUtil.getSimpleMessage(e), ExceptionUtil.stacktraceToString(e), null);
    }

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public Result<String> runtimeExceptionHandler(RuntimeException e) {
        logger.error("runtimeExceptionHandler", e);
        doErrorMetric();
        if (e instanceof BizException) {
            BizException bizException = (BizException) e;
            return Result.of(Status.INTERNAL_SERVER_ERROR, I18nUtil.getMessage(bizException.getI18nKey()), ExceptionUtil.stacktraceToString(bizException), null);
        }
        if (e instanceof DuplicateKeyException) {
            return Result.of(Status.DUPLICATE_KEY_EXCEPTION, I18nUtil.getMessage(Status.DUPLICATE_KEY_EXCEPTION.getI18nKey()), ExceptionUtil.stacktraceToString(e), null);
        }
        if (e instanceof DataIntegrityViolationException) {
            return Result.of(Status.DATA_INTEGRITY_VIOLATION_EXCEPTION, I18nUtil.getMessage(Status.DATA_INTEGRITY_VIOLATION_EXCEPTION.getI18nKey()), ExceptionUtil.stacktraceToString(e), null);
        }
        return Result.of(Status.INTERNAL_SERVER_ERROR, I18nUtil.getMessage(Status.INTERNAL_SERVER_ERROR.getI18nKey()), ExceptionUtil.stacktraceToString(e), null);
    }

    private void doErrorMetric() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();

        String path = getRequestPath(request);
        String method = request.getMethod();
        monitorFacade.counter(MetricConstant.HTTP_ERRORS_TOTAL, "method", method, "path", path).increment();
    }
}
