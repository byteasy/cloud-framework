package com.byteasy.dto;

import java.util.HashMap;
import java.util.Map;

public class FormatMap extends HashMap<String, Object> {
    private final Map<String, String> formatRules = new HashMap<>();

    public FormatMap() {
    }

    public FormatMap(Map<String, Object> origin) {
        putAll(origin);
    }

    public FormatMap rule(String key, String pattern) {
        formatRules.put(key, pattern);
        return this;
    }

    public FormatMap rules(Map<String, String> rules) {
        formatRules.putAll(rules);
        return this;
    }

    public Map<String, String> getFormatRules() {
        return formatRules;
    }
}
