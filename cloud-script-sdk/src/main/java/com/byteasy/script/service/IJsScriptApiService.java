package com.byteasy.script.service;

import com.byteasy.Result;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface IJsScriptApiService {

    Result runScript(String apiCode, Map<String, Object> params, Map<String, String> headers, String body) throws ExecutionException;

    Result debugScript(Map<String, Object> script, Map<String, Object> params, Map<String, String> headers, String body) throws ExecutionException;
}
