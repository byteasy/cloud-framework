package com.byteasy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Rest接口返回包裹类")
public class Result<T> {

    @Schema(description = "接口执行状态", implementation = Status.class)
    private int status;
    @Schema(description = "状态描述文本")
    private String description;
    @Schema(description = "错误")
    private String stackTrace;
    @Schema(description = "响应数据")
    private T data;
    @Schema(description = "时间戳(毫秒)")
    private Long timestamp = System.currentTimeMillis();

    public static <T> Result<T> success() {
        return of(Status.SUCCESS, Status.SUCCESS.getDescription(), null, null);
    }

    public static <T> Result<T> success(T data) {
        return of(Status.SUCCESS, Status.SUCCESS.getDescription(), null, data);
    }

    public static <T> Result<T> empty() {
        return of(Status.EMPTY, Status.EMPTY.getDescription(), null, null);
    }

    public static <T> Result<T> error() {
        return of(Status.INTERNAL_SERVER_ERROR, Status.INTERNAL_SERVER_ERROR.getDescription(), null, null);
    }

    public static <T> Result<T> error(Status status) {
        return of(status, status.getDescription(), null, null);
    }

    public static <T> Result<T> error(String description) {
        return of(Status.INTERNAL_SERVER_ERROR, description, null, null);
    }

    public static <T> Result<T> of() {
        return of(Status.SUCCESS, Status.SUCCESS.getDescription(), null, null);
    }

    public static <T> Result<T> of(Status status) {
        return of(status, status.getDescription(), null, null);
    }

    public static <T> Result<T> of(Status status, T data) {
        return of(status, status.getDescription(), null, data);
    }

    public static <T> Result<T> of(Status status, String description, String stackTrace, T data) {
        Result<T> result = new Result<T>();
        result.setStatus(status.getStatus());
        result.setDescription(description);
        result.setStackTrace(stackTrace);
        result.setData(data);
        return result;
    }

    public boolean isSuccess() {
        return Status.SUCCESS.getStatus() == status;
    }

    public boolean isFailed() {
        return Status.SUCCESS.getStatus() != status;
    }

    @JsonIgnore
    public <T> T getIfPresent() {
        if (isSuccess()) {
            return (T) data;
        }
        return null;
    }
}
