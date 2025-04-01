package com.imannuel.movin.notificationservice.kafka.listener;

import com.imannuel.movin.notificationservice.event.SendSingleEmailEvent;
import com.imannuel.movin.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendSingleEmailListener {
    private final EmailService emailService;

    @KafkaListener(
            topics = "${spring.kafka.topic.notification.email.single}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "sendSingleEmailEventConcurrentKafkaListenerContainerFactory"
    )
    public void handleCustomerVerified(SendSingleEmailEvent event) {
        log.info("Received send single email event for email {} with subject: {}", event.getTarget(), event.getSubject());
        try {
            emailService.sendOtpEmail(event.getTarget(), event.getSubject(), event.getContent());
            log.info("Success send email {} with subject: {}", event.getTarget(), event.getSubject());
        } catch (Exception e) {
            log.error("Error Send email {} with subject: {}", event.getTarget(), event.getSubject());
        }
    }
}
