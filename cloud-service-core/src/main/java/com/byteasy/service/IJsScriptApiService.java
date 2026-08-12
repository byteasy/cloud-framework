package com.byteasy.service;

import com.byteasy.Result;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface IJsScriptApiService {

    Result debugScript(String apiCode, Map<String, String[]> params, Map<String, String> headers, String body) throws ExecutionException;

    Result debugScript(Map<String, Object> script, Map<String, String[]> params, Map<String, String> headers, String body) throws ExecutionException;
}
