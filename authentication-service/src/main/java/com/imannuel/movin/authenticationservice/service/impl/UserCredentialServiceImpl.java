package com.imannuel.movin.authenticationservice.service.impl;

import com.imannuel.movin.authenticationservice.entity.UserCredential;
import com.imannuel.movin.authenticationservice.enums.RoleEnum;
import com.imannuel.movin.authenticationservice.exception.UserNotFoundException;
import com.imannuel.movin.authenticationservice.repository.UserCredentialRepository;
import com.imannuel.movin.authenticationservice.service.UserCredentialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCredentialServiceImpl implements UserCredentialService {
    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserCredential createUserCredential(String userId) {
        log.debug("Creating new user credential for user ID: {}", userId);
        UserCredential userCredential = UserCredential.builder()
                .userId(userId)
                .build();
        UserCredential saved = userCredentialRepository.saveAndFlush(userCredential);
        log.info("Successfully created user credential for user ID: {}", userId);
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public UserCredential findUserCredentialByUserId(String userId) {
        log.debug("Searching for user credential with user ID: {}", userId);
        try {
            UserCredential credential = userCredentialRepository.findByUserId(userId).orElseThrow(
                    () -> new UserNotFoundException("Invalid credentials")
            );
            log.debug("Found user credential for user ID: {}", userId);
            return credential;
        } catch (UserNotFoundException e) {
            log.warn("User credential not found for user ID: {}", userId);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setupUserCredential(String userId, String credential, String role) {
        log.debug("Setting up user credential for user ID: {} with role: {}", userId, role);
        UserCredential userCredential = findUserCredentialByUserId(userId);

        if (role.equals(RoleEnum.ROLE_CUSTOMER.name())) {
            log.debug("Setting PIN for customer user ID: {}", userId);
            userCredential.setPin(passwordEncoder.encode(credential));
            userCredentialRepository.save(userCredential);
            log.info("Successfully set PIN for customer user ID: {}", userId);
            return;
        }

        log.debug("Setting password for non-customer user ID: {}", userId);
        userCredential.setPassword(passwordEncoder.encode(credential));
        userCredentialRepository.save(userCredential);
        log.info("Successfully set password for non-customer user ID: {}", userId);
    }
}
