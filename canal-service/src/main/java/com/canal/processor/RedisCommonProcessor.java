package com.canal.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author gcq1109
 * @email gcq1109@126.com
 */
@Component
public class RedisCommonProcessor {

    @Autowired
    private RedisTemplate redisTemplate;

    public Object get(String key) {
        if (key == null) {
            throw new UnsupportedOperationException("error");
        }
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, Object value) {
        if (key == null) {
            throw new UnsupportedOperationException("error");
        }
        redisTemplate.opsForValue().set(key, value);
    }

    public void setTimeout(String key, Object value, long timeSeconds) {
        if (key == null) {
            throw new UnsupportedOperationException("error");
        }
        if (timeSeconds > 0) {
            redisTemplate.opsForValue().set(key, value, timeSeconds, TimeUnit.SECONDS);
        } else {
            set(key, value);
        }
    }

    public void setTimeoutDays(String key, Object value, long days) {
        if (key == null) {
            throw new UnsupportedOperationException("error");
        }
        if (days > 0) {
            redisTemplate.opsForValue().set(key, value, days, TimeUnit.DAYS);
        } else {
            set(key, value);
        }
    }

    public void remove(String key) {
        redisTemplate.delete(key);
    }
}
