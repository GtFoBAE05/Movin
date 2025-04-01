package org.imannuel.movin.userservice.kafka.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imannuel.movin.userservice.event.CustomerVerifiedEvent;
import org.imannuel.movin.userservice.service.CustomerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerVerifiedListener {
    private final CustomerService customerService;

    @KafkaListener(
            topics = "${spring.kafka.topic.customer.verified}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "customerVerifiedEventConcurrentKafkaListenerContainerFactory"
    )
    public void handleCustomerVerified(CustomerVerifiedEvent event) {
        String userId = event.getUserId();
        log.info("Received CustomerVerifiedEvent for user ID: {}", userId);
        try {
            customerService.updateOtpVerifiedStatus(userId, event.getVerificationType());
            log.info("Updated OTP verified status for user ID: {}", userId);
        } catch (Exception e) {
            log.error("Error updating OTP verified status for user ID: {}", userId, e);
            throw e;
        }
    }
}
