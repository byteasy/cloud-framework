package com.jiuyang.common;

public class Result<T> {
    private int status;
    private String description;
    private T data;

    public static <T> Result<T> success() {
        return of();
    }

    public static <T> Result<T> error() {
        return of(Status.INTERNAL_SERVER_ERROR);
    }

    public static <T> Result<T> error(String description) {
        return of(description);
    }

    public static <T> Result<T> of() {
        return of(Status.SUCCESS);
    }

    public static <T> Result<T> of(T data) {
        return of(Status.SUCCESS, data);
    }

    public static <T> Result<T> of(String description) {
        return of(Status.INTERNAL_SERVER_ERROR, description, null);
    }

    public static <T> Result<T> of(Status status) {
        return of(status, null);
    }

    public static <T> Result<T> of(Status status, T data) {
        return of(status, status.getDescription(), data);
    }

    public static <T> Result<T> of(Status status, String description, T data) {
        Result<T> result = new Result<T>();
        result.setStatus(status.getStatus());
        result.setDescription(description);
        result.setData(data);
        return result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
