package com.nettruyen.comic.service.Impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nettruyen.comic.service.IRedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RedisServiceImpl implements IRedisService {

    RedisTemplate<String, Object> redisTemplate;
    ObjectMapper objectMapper;


    @Override
    public <T> T getObject(String key, Class<T> clazz) {
        String json = (String) redisTemplate.opsForValue().get(key);
        if (json == null) {
            return null;
        }

        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize JSON from Redis", e);
        }
    }

    @Override
    public <T> void setObject(String key, T value, Integer timeout) {
        if (value == null) {
            return;
        }

        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json, Duration.ofSeconds(timeout));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }

    @Override
    public void deleteObject(String key) {
        redisTemplate.delete(key);
    }

}
