package com.byteasy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "状态定义")
public enum Status {
    SUCCESS(200, "statusOfSuccess", "Operation success"),
    EMPTY(204, "statusOfSuccessEmpty", "Operation success, but data is null"),

    AUTHENTICATION_FAILED(401, "statusOfAuthFailed", "Authentication failed"),
    AUTHORIZATION_FAILED(403, "statusOfAuthorizationFailed", "Authorization failed"),
    ARGS_MISSING(400, "statusOfArgsMissing", "Argument missing"),
    ARGS_VALIDATE_FAILED(422, "statusOfArgsValidateFailed", "Argument validate failed"),
    TOO_MANY_REQUESTS(429, "statusOfTooManyRequests", "Too Many Requests"),
    CONFLICT(409, "statusOfConflict", "Conflict"),
    DUPLICATE_KEY_EXCEPTION(409, "statusOfDuplicateKeyException", "Key Repeat"),
    DATA_INTEGRITY_VIOLATION_EXCEPTION(410, "statusOfDataIntegrityViolationException", "Database Key Violation"),

    INTERNAL_SERVER_ERROR(500, "statusOfServerError", "Internal Server Error"),
    BAD_GATEWAY(502, "statusOfBadGateway", "Bad Gateway"),
    SERVICE_UNAVAILABLE(503, "statusOfServiceUnavailable", "Service Unavailable"),
    GATEWAY_TIMEOUT(504, "statusOfTimeout", "Gateway Timeout");
    private final int status;
    private final String i18nKey;
    private final String description;

    Status(int status, String i18nKey, String description) {
        this.status = status;
        this.i18nKey = i18nKey;
        this.description = description;
    }
}
