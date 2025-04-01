package com.imannuel.movin.intrabanktransferservice.service.impl;

import com.imannuel.movin.intrabanktransferservice.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public <T> void save(String key, T value, Duration duration) {
        log.info("Saving key: {} with value: {} for {}", key, value, duration);
        redisTemplate.opsForValue().set(key, value, duration);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<T> get(String key) {
        log.info("Fetching key: {}", key);
        Object value = redisTemplate.opsForValue().get(key);
        log.info("Value of key {} is : {}", key, value);
        return Optional.ofNullable((T) value);
    }
}
