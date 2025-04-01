package org.imannuel.movin.verificationservice.service;

import java.time.Duration;
import java.util.Optional;

public interface RedisService {
    <T> void save(String key, T value, Duration duration);

    <T> Optional<T> get(String key);
}
