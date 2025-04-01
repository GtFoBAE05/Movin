package org.imannuel.movin.verificationservice.redis;

import lombok.extern.slf4j.Slf4j;
import org.imannuel.movin.verificationservice.service.OtpVerificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisExpirationListener extends KeyExpirationEventMessageListener {
    private final OtpVerificationService otpVerificationService;

    public RedisExpirationListener(RedisMessageListenerContainer listenerContainer, OtpVerificationService otpVerificationService) {
        super(listenerContainer);
        this.otpVerificationService = otpVerificationService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        log.info("Recived expired key: {}", expiredKey);

        if (expiredKey.startsWith("otp:")) {
            otpVerificationService.handleExpiredOtp(expiredKey);
        }


    }
}
