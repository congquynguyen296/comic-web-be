package com.nettruyen.comic.service;

public interface IRedisService {

    <T> T getObject(String key, Class<T> clazz);

    <T> void setObject(String key, T value, Integer timeout);

    void deleteObject(String key);
}
