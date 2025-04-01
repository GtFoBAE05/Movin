package com.imannuel.movin.authenticationservice.security;

import com.imannuel.movin.authenticationservice.dto.response.feign.internal.UserFeignResponse;
import com.imannuel.movin.authenticationservice.entity.UserCredential;
import com.imannuel.movin.authenticationservice.enums.RoleEnum;
import com.imannuel.movin.authenticationservice.exception.MissingCredentialException;
import com.imannuel.movin.authenticationservice.service.UserCredentialService;
import com.imannuel.movin.authenticationservice.service.UserFeignClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UserFeignClientService userFeignClientService;
    private final UserCredentialService userCredentialService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String phoneNumberOrEmail = authentication.getName();
        String credential = (String) authentication.getCredentials();

        log.debug("Attempting to authenticate user: {}", phoneNumberOrEmail);

        UserFeignResponse user = userFeignClientService.findUserByIdentifier(phoneNumberOrEmail);
        log.debug("User found: {}", user.getId());

        UserCredential userCredential = userCredentialService.findUserCredentialByUserId(user.getId());
        log.debug("User credentials fetched for user: {}", user.getId());

        if (!userCredential.hasCredentialsSetup()) {
            log.error("Credentials have not been set up yet for user: {}", user.getId());
            throw new MissingCredentialException("Credentials have not been set up yet");
        }

        if (user.getRole().getName().equals(RoleEnum.ROLE_CUSTOMER.name())) {
            log.debug("Validating PIN for customer role for user: {}", user.getId());
            if (!passwordEncoder.matches(credential, userCredential.getPin())) {
                log.error("Invalid PIN provided for user: {}", user.getId());
                throw new BadCredentialsException("Invalid credential");
            }
        } else {
            log.error("Unknown user type for user: {}", user.getId());
            throw new BadCredentialsException("Unknown user type");
        }

        log.info("User authenticated successfully: {}", user.getId());
        return new UsernamePasswordAuthenticationToken(user, "credential", new ArrayList<>());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}