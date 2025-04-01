package org.imannuel.movin.verificationservice.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imannuel.movin.verificationservice.enums.VerificationStatus;
import org.imannuel.movin.verificationservice.repository.VerificationStatusRepository;
import org.imannuel.movin.verificationservice.service.VerificationStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationStatusServiceImpl implements VerificationStatusService {
    private final VerificationStatusRepository verificationStatusRepository;

    @PostConstruct
    void init() {
        log.info("Initializing verification statuses");
        for (VerificationStatus.Status value : VerificationStatus.Status.values()) {
            verificationStatusRepository.findByName(value)
                    .orElseGet(() -> {
                        log.debug("Creating verification status: {}", value.name());
                        VerificationStatus verificationStatus = VerificationStatus.builder()
                                .name(value)
                                .build();
                        return verificationStatusRepository.save(verificationStatus);
                    });
        }
        log.info("Verification statuses initialization completed");
    }

    @Override
    public VerificationStatus findByName(String name) {
        log.debug("Finding verification status by name: {}", name);
        try {
            VerificationStatus.Status status = VerificationStatus.Status.valueOf(name);
            VerificationStatus verificationStatus = verificationStatusRepository.findByName(status)
                    .orElseThrow(() -> {
                        log.warn("Verification status not found: {}", name);
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Verification status not found");
                    });

            log.info("Successfully found verification status: {}", name);
            return verificationStatus;
        } catch (IllegalArgumentException e) {
            log.warn("Invalid verification status name requested: {}", name);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Verification status not found");
        }
    }

}
