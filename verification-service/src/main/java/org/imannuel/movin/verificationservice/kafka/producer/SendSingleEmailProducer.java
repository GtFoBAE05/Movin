package org.imannuel.movin.verificationservice.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.imannuel.movin.verificationservice.event.SendSingleEmailEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendSingleEmailProducer {
    private final KafkaTemplate<String, SendSingleEmailEvent> kafkaTemplate;

    @Value("${spring.kafka.topic.notification.email.single}")
    private String sendSingleEmailTopic;

    public void sendSendSingleEmailEvent(SendSingleEmailEvent sendSingleEmailEvent) {
        log.info("Sending send single email event for email {} with subject {}",
                sendSingleEmailEvent.getTarget(), sendSingleEmailEvent.getSubject());

        kafkaTemplate.send(sendSingleEmailTopic, sendSingleEmailEvent)
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        log.error("Failed to send single email event for email {} with subject {}",
                                sendSingleEmailEvent.getTarget(), sendSingleEmailEvent.getSubject(), throwable);
                    } else {
                        RecordMetadata metadata = result.getRecordMetadata();
                        log.info("Event sent for email: {} to topic: {} (offset: {}, partition: {})",
                                sendSingleEmailEvent.getTarget(), sendSingleEmailTopic, metadata.offset(), metadata.partition());
                    }
                });
    }
}

