package com.imannuel.movin.authenticationservice.service.impl;

import com.imannuel.movin.authenticationservice.dto.request.AuthenticationValidationRequest;
import com.imannuel.movin.authenticationservice.dto.request.CustomerRegisterRequest;
import com.imannuel.movin.authenticationservice.dto.request.LoginRequest;
import com.imannuel.movin.authenticationservice.dto.request.SetupCredentialRequest;
import com.imannuel.movin.authenticationservice.dto.response.AuthenticationValidationResponse;
import com.imannuel.movin.authenticationservice.dto.response.CustomerRegisterResponse;
import com.imannuel.movin.authenticationservice.dto.response.LoginResponse;
import com.imannuel.movin.authenticationservice.dto.response.feign.internal.CustomerRegistrationValidateFeignResponse;
import com.imannuel.movin.authenticationservice.dto.response.feign.internal.UserFeignResponse;
import com.imannuel.movin.authenticationservice.entity.UserCredential;
import com.imannuel.movin.authenticationservice.enums.RoleEnum;
import com.imannuel.movin.authenticationservice.exception.UserAlreadyRegisteredException;
import com.imannuel.movin.authenticationservice.repository.UserCredentialRepository;
import com.imannuel.movin.authenticationservice.service.AuthenticationService;
import com.imannuel.movin.authenticationservice.service.JwtService;
import com.imannuel.movin.authenticationservice.service.UserCredentialService;
import com.imannuel.movin.authenticationservice.service.UserFeignClientService;
import com.imannuel.movin.commonservice.utility.ValidationUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserFeignClientService userFeignClientService;
    private final UserCredentialRepository userCredentialRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserCredentialService userCredentialService;
    private final ValidationUtility validationUtility;

    @Override
    @Transactional(readOnly = true)
    public UserFeignResponse findUserByIdentifier(String identifier) {
        log.debug("Validating identifier: {}", identifier);
        validationUtility.validate(identifier);

        log.info("Fetching user by identifier: {}", identifier);
        UserFeignResponse user = userFeignClientService.findUserByIdentifier(identifier);
        log.debug("User found: {}", user.getId());
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest) {
        log.debug("Validating login request");
        validationUtility.validate(loginRequest);

        log.info("Attempting to authenticate user: {}", loginRequest.getIdentifier());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getIdentifier(), loginRequest.getCredential()));

        UserFeignResponse userCredential = (UserFeignResponse) authentication.getPrincipal();
        log.debug("User authenticated successfully: {}", userCredential.getId());

        log.info("Generating JWT token for user: {}", userCredential.getId());
        String generatedToken = jwtService.generateToken(userCredential);
        log.debug("JWT token generated successfully");

        return LoginResponse.builder()
                .accessToken(generatedToken)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerRegisterResponse customerRegister(CustomerRegisterRequest customerRegisterRequest) {
        log.debug("Validating customer registration request");
        validationUtility.validate(customerRegisterRequest);

        log.info("Checking phone number availability: {}", customerRegisterRequest.getPhoneNumber());
        CustomerRegistrationValidateFeignResponse phoneNumberAvailability = userFeignClientService
                .registerPhoneNumberAvailability(customerRegisterRequest.getPhoneNumber());

        log.info("Checking email availability: {}", customerRegisterRequest.getEmail());
        CustomerRegistrationValidateFeignResponse emailAvailability = userFeignClientService
                .registerEmailAvailability(customerRegisterRequest.getEmail());

        if (phoneNumberAvailability.isRegistered() || emailAvailability.isRegistered()) {
            log.error("User already registered with phone number or email");
            throw new UserAlreadyRegisteredException("User already registered");
        }

        log.info("Registering new customer");
        CustomerRegisterResponse customer = userFeignClientService.registerCustomer(customerRegisterRequest);
        log.debug("Customer registered successfully: {}", customer.getId());

        log.info("Creating user credential for customer: {}", customer.getId());
        UserCredential userCredential = UserCredential.builder()
                .userId(customer.getId())
                .build();
        userCredentialRepository.save(userCredential);
        log.debug("User credential created successfully");

        return customer;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setupCredential(SetupCredentialRequest setupCredentialRequest) {
        log.debug("Validating setup credential request");
        validationUtility.validate(setupCredentialRequest);

        log.info("Fetching user by identifier: {}", setupCredentialRequest.getIdentifier());
        UserFeignResponse user = userFeignClientService.findUserByIdentifier(setupCredentialRequest.getIdentifier());
        log.debug("User found: {}", user.getId());

        String role = user.getRole().getName().equals(RoleEnum.ROLE_CUSTOMER.name()) ?
                RoleEnum.ROLE_CUSTOMER.name() : RoleEnum.ROLE_ADMIN.name();
        log.info("Setting up credential for user: {} with role: {}", user.getId(), role);

        userCredentialService.setupUserCredential(user.getId(), setupCredentialRequest.getCredential(), role);
        log.debug("Credential setup successfully for user: {}", user.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public AuthenticationValidationResponse validateAuthentication(AuthenticationValidationRequest authenticationValidationRequest) {
        log.debug("Validating authentication validation request");
        validationUtility.validate(authenticationValidationRequest);

        log.info("Extracting user ID from token");
        String token = authenticationValidationRequest.getToken();
        String userId = jwtService.extractSubject(token);
        log.debug("User ID extracted: {}", userId);

        log.info("Fetching user by ID: {}", userId);
        UserFeignResponse user = userFeignClientService.findUserByUserId(userId);
        log.debug("User found: {}", user.getId());

        return AuthenticationValidationResponse.builder()
                .userId(user.getId())
                .role(user.getRole().getName())
                .build();
    }
}
