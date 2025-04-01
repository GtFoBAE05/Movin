package org.imannuel.movin.verificationservice.service.impl;

import com.imannuel.movin.commonservice.utility.ValidationUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imannuel.movin.verificationservice.dto.request.SendOtpRequest;
import org.imannuel.movin.verificationservice.dto.request.VerifyOtpRequest;
import org.imannuel.movin.verificationservice.dto.response.feign.internal.UserFeignResponse;
import org.imannuel.movin.verificationservice.entity.OtpVerification;
import org.imannuel.movin.verificationservice.enums.OtpMethod;
import org.imannuel.movin.verificationservice.enums.VerificationStatus;
import org.imannuel.movin.verificationservice.event.CustomerVerifiedEvent;
import org.imannuel.movin.verificationservice.event.SendSingleEmailEvent;
import org.imannuel.movin.verificationservice.kafka.producer.CustomerVerifiedProducer;
import org.imannuel.movin.verificationservice.kafka.producer.SendSingleEmailProducer;
import org.imannuel.movin.verificationservice.repository.OtpVerificationRepository;
import org.imannuel.movin.verificationservice.service.*;
import org.imannuel.movin.verificationservice.utility.EmailTemplateLoader;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpVerificationServiceImpl implements OtpVerificationService {
    private final OtpVerificationRepository otpVerificationRepository;
    private final OtpMethodService otpMethodService;
    private final VerificationStatusService verificationStatusService;
    private final ValidationUtility validationUtility;
    private final UserFeignClientService userFeignClientService;
    private final CustomerVerifiedProducer customerVerifiedProducer;
    private final SendSingleEmailProducer sendSingleEmailProducer;
    private final RedisService redisService;

    @Override
    @Transactional(readOnly = true)
    public OtpVerification findById(String id) {
        log.debug("Finding OTP verification for id: {}", id);

        return otpVerificationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Invalid OTP attempt for id: {}", id);
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP");
                });
    }

    @Override
    @Transactional(readOnly = true)
    public OtpVerification findByUserIdAndOtpMethodAndVerificationCode(String userId, String method, String verificationCode) {
        log.debug("Finding OTP verification for userId: {}, method: {}", userId, method);
        OtpMethod otpMethod = otpMethodService.findByName(method);

        return otpVerificationRepository.findByUserIdAndOtpMethodAndVerificationCode(userId, otpMethod, verificationCode)
                .orElseThrow(() -> {
                    log.warn("Invalid OTP attempt for userId: {}, method: {}, code: {}", userId, method, verificationCode);
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP");
                });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void sendOtp(SendOtpRequest sendOtpRequest) {
        validationUtility.validate(sendOtpRequest);

        String identifier = sendOtpRequest.getIdentifier();
        String method = sendOtpRequest.getMethod();

        if (!Objects.equals(method, "EMAIL")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currently only support via email");
        }

        log.info("Processing OTP request for user with identifier: {}, method: {}", identifier, method);
        UserFeignResponse user = userFeignClientService.findUserByIdentifier(identifier);
        checkRecentOtpRequest(user.getId());

        String otp = generateOtp();
        OtpMethod otpMethod = otpMethodService.findByName(method);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryTime = now.plusMinutes(5);
        VerificationStatus pendingStatus = verificationStatusService.findByName(VerificationStatus.Status.PENDING.name());

        log.debug("Generated OTP: {} for userId: {}, expires at: {}", otp, user.getId(), expiryTime);

        OtpVerification otpVerification = OtpVerification.builder()
                .otpMethod(otpMethod)
                .userId(user.getId())
                .verificationCode(otp)
                .verificationSentAt(now)
                .verificationExpiresAt(expiryTime)
                .verificationStatus(pendingStatus)
                .build();

        log.info("Saving OTP record for userId: {}", user.getId());
        otpVerificationRepository.save(otpVerification);

        try {
            log.debug("Sending OTP via email for userId: {}", user.getId());
            sendOtpByEmail(user.getEmail(), otp);
            redisService.save(String.format("otp:%s", otpVerification.getId()),
                    String.format("%s:%s:%s", otpVerification.getVerificationCode(), otpVerification.getUserId(), otpVerification.getOtpMethod().getName().name()),
                    Duration.ofMinutes(5));
            log.info("OTP successfully sent for userId: {}, expires at: {}", user.getId(), expiryTime);
        } catch (Exception e) {
            log.error("Failed to send OTP for userId: {}. Error: {}", user.getId(), e.getMessage(), e);
            otpVerification.setErrorMessage(e.getMessage());
            otpVerificationRepository.save(otpVerification);
            updateOtpStatus(otpVerification, VerificationStatus.Status.ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void verifyOtp(VerifyOtpRequest verifyOtpRequest) {
        String identifier = verifyOtpRequest.getIdentifier();
        String method = verifyOtpRequest.getMethod();
        String otp = verifyOtpRequest.getOtp();

        UserFeignResponse user = userFeignClientService.findUserByIdentifier(identifier);

        log.info("Verifying OTP for userId: {}, method: {}", user.getId(), method);
        validationUtility.validate(verifyOtpRequest);

        userFeignClientService.findUserByUserId(user.getId());

        OtpVerification otpVerification = findByUserIdAndOtpMethodAndVerificationCode(user.getId(), method, otp);

        if (LocalDateTime.now().isAfter(otpVerification.getVerificationExpiresAt())) {
            log.warn("OTP expired for userId: {}, method: {}", user.getId(), method);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP has expired");
        }

        log.info("OTP verified successfully for userId: {}, method: {}", user.getId(), method);
        updateOtpStatus(otpVerification, VerificationStatus.Status.USED);

        CustomerVerifiedEvent customerVerifiedEvent = CustomerVerifiedEvent.builder()
                .userId(user.getId())
                .VerificationType(verifyOtpRequest.getMethod())
                .build();

        log.debug("Publishing customer verified event for userId: {}", user.getId());
        customerVerifiedProducer.sendCustomerVerified(customerVerifiedEvent);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateOtpStatus(OtpVerification otpVerification, VerificationStatus.Status status) {
        log.info("Updating OTP status for userId: {} to {}", otpVerification.getUserId(), status);
        VerificationStatus verificationStatus = verificationStatusService.findByName(status.name());
        otpVerification.setVerificationStatus(verificationStatus);
        otpVerificationRepository.save(otpVerification);
        log.debug("Successfully updated OTP status for userId: {}", otpVerification.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleExpiredOtp(String key) {
        log.info("Handling expired OTP for key: {}", key);

        String[] keyParts = key.split(":");
        if (keyParts.length < 2) {
            log.warn("Invalid key format: {}", key);
            return;
        }
        String id = keyParts[1];

        OtpVerification otpVerification = findById(id);
        VerificationStatus.Status otpVerificationStatus = otpVerification.getVerificationStatus().getName();
        String userId = otpVerification.getUserId();

        if (!otpVerificationStatus.equals(VerificationStatus.Status.PENDING)) {
            log.warn("OTP [{}] is already in status: {}, skipping expiration handling.", id, otpVerificationStatus);
            return;
        }

        updateOtpStatus(otpVerification, VerificationStatus.Status.EXPIRED);
        log.info("OTP [{}] expired successfully for userId: {}", id, userId);
    }

    @Transactional(readOnly = true)
    public void checkRecentOtpRequest(String userId) {
        Integer requestCount = otpVerificationRepository.countAllByUserIdAndVerificationSentAtAfter(userId, LocalDateTime.now().minusMinutes(5));

        if (requestCount >= 3) {
            log.warn("Too many OTP requests for userId: {}, count: {}", userId, requestCount);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Too many recent requests. Please try again in a few moments");
        }
    }

    private void sendOtpByEmail(String target, String otpCode) {
        log.info("Preparing OTP email for target: {}", target);

        String otpTemplate = EmailTemplateLoader.loadTemplate("templates/otp_template.html");
        log.info("Successfully loaded email template.");

        otpTemplate = otpTemplate.replace("{{otp}}", otpCode);

        SendSingleEmailEvent sendSingleEmailEvent = SendSingleEmailEvent.builder()
                .target(target)
                .subject("Your OTP code from Movin")
                .content(otpTemplate)
                .build();

        sendSingleEmailProducer.sendSendSingleEmailEvent(sendSingleEmailEvent);
    }


    private String generateOtp() {
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));
        log.debug("Generated OTP: {}", otp);
        return otp;
    }
}