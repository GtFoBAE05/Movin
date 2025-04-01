package com.imannuel.movin.authenticationservice.service.impl;

import com.imannuel.movin.authenticationservice.client.UserFeignClient;
import com.imannuel.movin.authenticationservice.dto.request.CustomerRegisterRequest;
import com.imannuel.movin.authenticationservice.dto.response.CustomerRegisterResponse;
import com.imannuel.movin.authenticationservice.dto.response.feign.internal.CustomerRegistrationValidateFeignResponse;
import com.imannuel.movin.authenticationservice.dto.response.feign.internal.UserFeignResponse;
import com.imannuel.movin.authenticationservice.service.UserFeignClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserFeignClientServiceImpl implements UserFeignClientService {
    private final UserFeignClient userFeignClient;

    @Override
    public CustomerRegistrationValidateFeignResponse registerPhoneNumberAvailability(String phone) {
        log.debug("Checking phone number availability: {}", phone);
        try {
            CustomerRegistrationValidateFeignResponse result = userFeignClient.registerPhoneNumberAvailability(phone);
            log.debug("Phone number availability check completed for: {}", phone);
            return result;
        } catch (Exception e) {
            log.error("Error checking phone number availability for: {}", phone, e);
            throw e;
        }
    }

    @Override
    public CustomerRegistrationValidateFeignResponse registerEmailAvailability(String email) {
        log.debug("Checking email availability: {}", email);
        try {
            CustomerRegistrationValidateFeignResponse result = userFeignClient.registerEmailAvailability(email);
            log.debug("Email availability check completed for: {}", email);
            return result;
        } catch (Exception e) {
            log.error("Error checking email availability for: {}", email, e);
            throw e;
        }
    }

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

    @Override
    public CustomerRegisterResponse registerCustomer(CustomerRegisterRequest customerRegisterRequest) {
        log.info("Registering new customer with email: {}", customerRegisterRequest.getEmail());
        try {
            CustomerRegisterResponse response = userFeignClient.registerCustomer(customerRegisterRequest);
            log.info("Successfully registered new customer with ID: {}", response.getId());
            return response;
        } catch (Exception e) {
            log.error("Failed to register customer with email: {}", customerRegisterRequest.getEmail(), e);
            throw e;
        }
    }
}
