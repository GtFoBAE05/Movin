package org.imannuel.movin.verificationservice.controller;

import com.imannuel.movin.commonservice.dto.mapper.ResponseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imannuel.movin.verificationservice.dto.request.SendOtpRequest;
import org.imannuel.movin.verificationservice.dto.request.VerifyOtpRequest;
import org.imannuel.movin.verificationservice.service.OtpVerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/verification")
@RequiredArgsConstructor
@Slf4j
public class OtpVerificationController {
    private final OtpVerificationService otpVerificationService;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(
            @RequestBody SendOtpRequest sendOtpRequest
    ) {
        log.info("Received request to send OTP for user with identifier {}", sendOtpRequest.getIdentifier());
        otpVerificationService.sendOtp(sendOtpRequest);
        log.info("Successfully sent OTP to user with identifier {}", sendOtpRequest.getIdentifier());
        return ResponseMapper.toCommonResponse(true, HttpStatus.OK, "Success send OTP", null);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @RequestBody VerifyOtpRequest verifyOtpRequest
    ) {
        log.info("Received request to verify OTP for user with identifier {}", verifyOtpRequest.getIdentifier());
        otpVerificationService.verifyOtp(verifyOtpRequest);
        log.info("Successfully verified OTP for user with identifier {}", verifyOtpRequest.getIdentifier());
        return ResponseMapper.toCommonResponse(true, HttpStatus.OK, "Success verify OTP", null);
    }
}

