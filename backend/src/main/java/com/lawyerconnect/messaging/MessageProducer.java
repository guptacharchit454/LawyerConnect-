package com.lawyerconnect.messaging;

import com.lawyerconnect.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageProducer {

    private final KafkaTemplate<String, MessageDto> kafkaTemplate;
    private static final String TOPIC = "lawyerconnect-chat";

    /**
     * Publishes a chat message event to Kafka asynchronously.
     * Uses the caseId as the message key to route all messages for a specific case 
     * to the same partition, guaranteeing chronological order preservation.
     */
    public void sendMessage(MessageDto messageDto) {
        String partitionKey = messageDto.getCaseId().toString();
        
        log.info("Publishing chat message to Kafka topic '{}' with key '{}'", TOPIC, partitionKey);
        
        CompletableFuture<SendResult<String, MessageDto>> future = 
                kafkaTemplate.send(TOPIC, partitionKey, messageDto);
                
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Message sent successfully to partition [{}] with offset [{}]", 
                        result.getRecordMetadata().partition(), 
                        result.getRecordMetadata().offset());
            } else {
                log.error("Failed to publish message to Kafka topic '{}' due to error: ", 
                        TOPIC, ex);
            }
        });
    }
}
