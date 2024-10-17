package com.jiuyang.common;

public enum Status {
    SUCCESS(200, "success"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    AUTHENTICATION_FAILED(501, "authentication failed"),
    AUTHORIZATION_FAILED(502, "authorization failed"),
    ARGS_MISSING(503, "argument missing"),
    ARGS_VALIDATE_FAILED(504, "argument validate failed"),
    ;
    private final int status;
    private final String description;

    Status(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
