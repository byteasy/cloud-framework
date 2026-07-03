package com.byteasy.common.util;

import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4j2HttpLoggingInterceptor implements HttpLoggingInterceptor.Logger {

    private static final Logger logger = LogManager.getLogger(Log4j2HttpLoggingInterceptor.class);

    @Override
    public void log(String message) {
        // 根据消息内容决定日志级别
        if (message.startsWith("-->") || message.startsWith("<--")) {
            logger.debug(message);
        } else if (message.contains("HEADERS")) {
            logger.trace(message);
        } else if (message.contains("BODY")) {
            logger.debug(message);
        } else {
            logger.info(message);
        }
    }
}
