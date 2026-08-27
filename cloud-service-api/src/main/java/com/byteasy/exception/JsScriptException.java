package com.byteasy.exception;

public class JsScriptException extends RuntimeException {

    public JsScriptException() {
    }

    public JsScriptException(String message) {
        super(message);
    }

    public JsScriptException(Throwable cause) {
        super(cause);
    }

    public JsScriptException(String message, Throwable cause) {
        super(message, cause);
    }
}
