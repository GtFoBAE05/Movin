package org.imannuel.movin.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imannuel.movin.userservice.dto.response.CustomerRegistrationValidate;
import org.imannuel.movin.userservice.entity.Customer;
import org.imannuel.movin.userservice.entity.User;
import org.imannuel.movin.userservice.repository.UserRepository;
import org.imannuel.movin.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public User findById(String id) {
        log.debug("Finding user by ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User with ID {} not found", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            String.format("User with ID %s not found", id));
                });

        log.info("Successfully found user with ID: {}", id);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User findByPhoneNumberOrEmail(String phoneNumberOrEmail) {
        log.debug("Finding user by phone number or email: {}", phoneNumberOrEmail);

        User user = userRepository.findByPhoneNumberOrEmail(phoneNumberOrEmail, phoneNumberOrEmail)
                .orElseThrow(() -> {
                    log.warn("User with phone/email {} not found", phoneNumberOrEmail);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            String.format("User with phone/email %s not found", phoneNumberOrEmail));
                });

        log.info("Successfully found user with phone/email: {}", phoneNumberOrEmail);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerRegistrationValidate validateUserAvailability(String identifier, boolean isPhone) {
        String identifierType = isPhone ? "phone number" : "email";
        log.debug("Validating user availability by {}: {}", identifierType, identifier);

        Optional<User> user = isPhone
                ? userRepository.findByPhoneNumber(identifier)
                : userRepository.findByEmail(identifier);

        if (user.isPresent() && user.get() instanceof Customer customer) {
            log.info("Customer found with {}: {}, phone verified: {}, email verified: {}",
                    identifierType, identifier, customer.isPhoneVerified(), customer.isEmailVerified());

            return CustomerRegistrationValidate.builder()
                    .registered(true)
                    .phoneVerification(customer.isPhoneVerified())
                    .emailVerification(customer.isEmailVerified())
                    .build();
        }

        log.info("No customer found with {}: {}", identifierType, identifier);
        return CustomerRegistrationValidate.builder()
                .registered(false)
                .phoneVerification(false)
                .emailVerification(false)
                .pinSetup(false)
                .build();
    }
}