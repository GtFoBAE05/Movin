package org.imannuel.movin.verificationservice.service;

import org.imannuel.movin.verificationservice.dto.request.SendOtpRequest;
import org.imannuel.movin.verificationservice.dto.request.VerifyOtpRequest;
import org.imannuel.movin.verificationservice.entity.OtpVerification;

public interface OtpVerificationService {
    OtpVerification findById(String id);

    OtpVerification findByUserIdAndOtpMethodAndVerificationCode(String userId, String method, String verificationCode);

    void sendOtp(SendOtpRequest sendOtpRequest);

    void verifyOtp(VerifyOtpRequest verifyOtpRequest);

    void handleExpiredOtp(String key);
}
