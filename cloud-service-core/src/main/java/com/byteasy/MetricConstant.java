package com.byteasy;

public interface MetricConstant {

    /**
     * 接口请求累计数量
     */
    String HTTP_REQUESTS_TOTAL = "http_requests_total";
    /**
     * 接口请求错误数量
     */
    String HTTP_ERRORS_TOTAL = "http_errors_total";
    /**
     * 接口调用延时
     */
    String HTTP_LATENCY = "http_requests_latency";
}
