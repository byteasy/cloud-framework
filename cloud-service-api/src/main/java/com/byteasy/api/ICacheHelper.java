package com.byteasy.api;

import tools.jackson.core.type.TypeReference;

import java.util.concurrent.TimeUnit;

public interface ICacheHelper {

    void put(String cacheName, String key, Object value, long lifeSpan, TimeUnit unit);

    void put(String cacheName, String key, Object value);

    <T> T get(String cacheName, String key, TypeReference<T> type);

    String getStr(String cacheName, String key);

    String removeStr(String cacheName, String key);

    <T> T remove(String cacheName, String key, TypeReference<T> type);
}
