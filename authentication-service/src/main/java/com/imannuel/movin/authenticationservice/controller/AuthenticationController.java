package com.imannuel.movin.authenticationservice.controller;

import com.imannuel.movin.authenticationservice.dto.request.AuthenticationValidationRequest;
import com.imannuel.movin.authenticationservice.dto.request.CustomerRegisterRequest;
import com.imannuel.movin.authenticationservice.dto.request.LoginRequest;
import com.imannuel.movin.authenticationservice.dto.request.SetupCredentialRequest;
import com.imannuel.movin.authenticationservice.dto.response.AuthenticationValidationResponse;
import com.imannuel.movin.authenticationservice.dto.response.CustomerRegisterResponse;
import com.imannuel.movin.authenticationservice.dto.response.LoginResponse;
import com.imannuel.movin.authenticationservice.service.AuthenticationService;
import com.imannuel.movin.commonservice.dto.mapper.ResponseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/customer/register")
    public ResponseEntity<?> registerCustomer(
            @RequestBody CustomerRegisterRequest customerRegisterRequest
    ) {
        log.info("Received request to register customer with email/phone: {} / {}", customerRegisterRequest.getEmail(), customerRegisterRequest.getPhoneNumber());
        CustomerRegisterResponse customerRegisterResponse = authenticationService.customerRegister(customerRegisterRequest);
        log.debug("Customer registered successfully: {}", customerRegisterResponse);
        return ResponseMapper.toCommonResponse(true, HttpStatus.CREATED, "Success register customer", customerRegisterResponse);
    }

    @PostMapping("/credential/setup")
    public ResponseEntity<?> setupPin(
            @RequestBody SetupCredentialRequest setupCredentialRequest
    ) {
        log.info("Received request to setup credential for user: {}", setupCredentialRequest.getIdentifier());
        authenticationService.setupCredential(setupCredentialRequest);
        log.debug("Credential setup successfully for user: {}", setupCredentialRequest.getIdentifier());
        return ResponseMapper.toCommonResponse(true, HttpStatus.OK, "Successfully setup credential", null);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest loginRequest
    ) {
        log.info("Received login request for user: {}", loginRequest.getIdentifier());
        LoginResponse loginResponse = authenticationService.login(loginRequest);
        log.debug("Login successful for user: {}", loginRequest.getIdentifier());
        return ResponseMapper.toCommonResponse(true, HttpStatus.OK, "Successfully login", loginResponse);
    }

    @PostMapping("/validate-token")
    public AuthenticationValidationResponse validateAuthentication(
            @RequestBody AuthenticationValidationRequest authenticationValidationRequest
    ) {
        log.info("Received request to validate token");
        AuthenticationValidationResponse response = authenticationService.validateAuthentication(authenticationValidationRequest);
        log.debug("Token validation successful for user: {}", response.getUserId());
        return response;
    }
}