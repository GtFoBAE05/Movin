package com.imannuel.movin.authenticationservice.service;

import com.imannuel.movin.authenticationservice.dto.request.CustomerRegisterRequest;
import com.imannuel.movin.authenticationservice.dto.response.CustomerRegisterResponse;
import com.imannuel.movin.authenticationservice.dto.response.feign.internal.CustomerRegistrationValidateFeignResponse;
import com.imannuel.movin.authenticationservice.dto.response.feign.internal.UserFeignResponse;

public interface UserFeignClientService {
    CustomerRegistrationValidateFeignResponse registerPhoneNumberAvailability(String phone);

    CustomerRegistrationValidateFeignResponse registerEmailAvailability(String email);

    UserFeignResponse findUserByIdentifier(String identifier);

    UserFeignResponse findUserByUserId(String userId);

    CustomerRegisterResponse registerCustomer(CustomerRegisterRequest customerRegisterRequest);
}
