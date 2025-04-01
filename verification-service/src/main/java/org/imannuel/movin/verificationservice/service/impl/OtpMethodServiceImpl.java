package org.imannuel.movin.verificationservice.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imannuel.movin.verificationservice.enums.OtpMethod;
import org.imannuel.movin.verificationservice.repository.OtpMethodRepository;
import org.imannuel.movin.verificationservice.service.OtpMethodService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpMethodServiceImpl implements OtpMethodService {
    private final OtpMethodRepository otpMethodRepository;

    @PostConstruct
    private void init() {
        log.info("Starting OTP methods initialization");

        Arrays.stream(OtpMethod.Method.values())
                .filter(method -> !otpMethodRepository.existsByName(method))
                .forEach(method -> {
                    log.debug("Creating OTP method: {}", method.name());
                    otpMethodRepository.save(OtpMethod.builder().name(method).build());
                    log.info("Successfully created OTP method: {}", method.name());
                });

        log.info("OTP methods initialization completed");
    }

    @Override
    @Transactional(readOnly = true)
    public OtpMethod findByName(String name) {
        log.debug("Searching for OTP method with name: {}", name);

        try {
            OtpMethod.Method method = OtpMethod.Method.valueOf(name);
            return otpMethodRepository.findByName(method)
                    .orElseThrow(() -> {
                        log.warn("OTP method not found: {}", name);
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, "OTP method " + name + " not found");
                    });
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Invalid OTP method name: %s", name));
        }

    }
}
