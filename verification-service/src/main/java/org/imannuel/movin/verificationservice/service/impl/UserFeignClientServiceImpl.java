package org.imannuel.movin.verificationservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imannuel.movin.verificationservice.client.UserFeignClient;
import org.imannuel.movin.verificationservice.dto.response.feign.internal.UserFeignResponse;
import org.imannuel.movin.verificationservice.service.UserFeignClientService;
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

    @Override
    public UserFeignResponse findUserByIdentifier(String identifier) {
        log.debug("Finding user by identifier: {}", identifier);
        try {
            UserFeignResponse user = userFeignClient.findUserByIdentifier(identifier);
            log.debug("User found with identifier: {}", identifier);
            return user;
        } catch (Exception e) {
            log.error("Error finding user with identifier: {}", identifier, e);
            throw e;
        }
    }
}
