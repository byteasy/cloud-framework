package com.byteasy.component;

import java.util.concurrent.ExecutionException;

public interface ICacheLoader<K, T> {

    T load(K key) throws ExecutionException;

    void invalid(Object entity);

    String cacheType();

    String[] invalidType();
}
