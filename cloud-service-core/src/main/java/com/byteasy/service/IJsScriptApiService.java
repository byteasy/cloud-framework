package com.byteasy.service;

import com.byteasy.Result;

import java.util.Map;

public interface IJsScriptApiService {

    Result runApiScript(Long apiId, Map<String, String[]> params, Map<String, String> headers, String body);
}
