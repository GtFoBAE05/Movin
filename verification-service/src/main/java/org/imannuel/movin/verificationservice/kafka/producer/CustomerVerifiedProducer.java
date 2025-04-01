package org.imannuel.movin.verificationservice.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.imannuel.movin.verificationservice.event.CustomerVerifiedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerVerifiedProducer {
    private final KafkaTemplate<String, CustomerVerifiedEvent> kafkaTemplate;

    @Value("${spring.kafka.topic.customer.verified}")
    private String userVerifiedTopic;

    public void sendCustomerVerified(CustomerVerifiedEvent customerVerifiedEvent) {
        log.info("Sending customer verified event for user ID: {}", customerVerifiedEvent.getUserId());

        kafkaTemplate.send(userVerifiedTopic, customerVerifiedEvent)
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        log.error("Failed to send event for user ID: {} to topic: {}",
                                customerVerifiedEvent.getUserId(), userVerifiedTopic, throwable);
                    } else {
                        RecordMetadata metadata = result.getRecordMetadata();
                        log.info("Event sent for user ID: {} to topic: {} (offset: {}, partition: {})",
                                customerVerifiedEvent.getUserId(), userVerifiedTopic, metadata.offset(), metadata.partition());
                    }
                });
    }
}

