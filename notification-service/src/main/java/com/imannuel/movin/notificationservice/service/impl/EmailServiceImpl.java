package com.imannuel.movin.notificationservice.service.impl;

import com.imannuel.movin.notificationservice.service.EmailService;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final Resend resend;

    @Value("${resend.email.domain}")
    private String resendDomain;

    @Override
    public void sendOtpEmail(String target, String subject, String content) throws ResendException {
        log.info("Attempting send email to {} with subject {}", target, subject);

        CreateEmailOptions createEmailOptions = CreateEmailOptions
                .builder()
                .from(resendDomain)
                .to(target)
                .subject(subject)
                .html(content)
                .build();

        try {
            resend.emails().send(createEmailOptions);
            log.info("Successfully sent email to {} with subject {}", target, subject);
        } catch (ResendException e) {
            log.error("Failed to send email to {} with subject {}", target, subject, e);
            throw e;
        }
    }
}
