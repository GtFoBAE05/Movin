package com.imannuel.movin.intrabanktransferservice.service.impl;

import com.imannuel.movin.intrabanktransferservice.client.UserFeignClient;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.internal.UserFeignResponse;
import com.imannuel.movin.intrabanktransferservice.service.UserFeignClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserFeignClientServiceImpl implements UserFeignClientService {
    private final UserFeignClient userFeignClient;

    @Override
    public UserFeignResponse findUserByUserId(String userId) {
        log.debug("Finding user by ID: {}", userId);
        try {
            UserFeignResponse user = userFeignClient.findUserById(userId);
            log.debug("User found with ID: {}", userId);
            return user;
        } catch (Exception e) {
            log.error("Error finding user with ID: {}", userId, e);
            throw e;
        }
    }
}
