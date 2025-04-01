package com.imannuel.movin.notificationservice.service;

import com.resend.core.exception.ResendException;

public interface EmailService {
    public void sendOtpEmail(String target, String subject, String content) throws ResendException;
}
