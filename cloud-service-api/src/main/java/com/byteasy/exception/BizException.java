package com.byteasy.exception;

/**
 * 业务操作异常，前端只显示输入的消息，不打印堆栈
 */
public class BizException extends RuntimeException {

    private String i18nKey;

    public BizException() {
    }

    public BizException(String i18nKey) {
        super(i18nKey);
        this.i18nKey = i18nKey;
    }

    public BizException(String i18nKey, Throwable cause) {
        super(i18nKey, cause);
        this.i18nKey = i18nKey;
    }

    public String getI18nKey() {
        return i18nKey;
    }
}
