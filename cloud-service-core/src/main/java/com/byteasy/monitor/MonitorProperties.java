package com.byteasy.monitor;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties("monitor")
@Data
public class MonitorProperties {

    private Map<String, String> tags = new HashMap<>();
    private int maxTagsPerMetric = 10;
    private String metricNameForCardinalityLimit = "http.server.requests";
    private String tagKeyForCardinalityLimit = "uri";
    private int maxTagCardinality = 100;

}