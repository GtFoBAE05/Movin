package com.imannuel.movin.authenticationservice.service;

import com.imannuel.movin.authenticationservice.dto.request.AuthenticationValidationRequest;
import com.imannuel.movin.authenticationservice.dto.request.CustomerRegisterRequest;
import com.imannuel.movin.authenticationservice.dto.request.LoginRequest;
import com.imannuel.movin.authenticationservice.dto.request.SetupCredentialRequest;
import com.imannuel.movin.authenticationservice.dto.response.AuthenticationValidationResponse;
import com.imannuel.movin.authenticationservice.dto.response.CustomerRegisterResponse;
import com.imannuel.movin.authenticationservice.dto.response.LoginResponse;
import com.imannuel.movin.authenticationservice.dto.response.feign.internal.UserFeignResponse;

public interface AuthenticationService {
    UserFeignResponse findUserByIdentifier(String identifier);

    CustomerRegisterResponse customerRegister(CustomerRegisterRequest customerRegisterRequest);

    LoginResponse login(LoginRequest loginRequest);

    void setupCredential(SetupCredentialRequest setupCredentialRequest);

    AuthenticationValidationResponse validateAuthentication(AuthenticationValidationRequest authenticationValidationRequest);
}
