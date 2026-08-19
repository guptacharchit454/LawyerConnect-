package com.lawyerconnect.messaging;

import com.lawyerconnect.dto.MessageDto;
import com.lawyerconnect.model.Case;
import com.lawyerconnect.model.Message;
import com.lawyerconnect.repository.CaseRepository;
import com.lawyerconnect.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;
    private final CaseRepository caseRepository;

    /**
     * Ingests messages from the "lawyerconnect-chat" Kafka topic.
     * 1. Broadcasts the payload over WebSockets to /queue/messages-{caseId}.
     * 2. Converts the DTO into a JPA entity and persists it into the PostgreSQL database.
     */
    @KafkaListener(topics = "lawyerconnect-chat", groupId = "chat-group")
    @Transactional
    public void consumeChatMessage(MessageDto messageDto) {
        log.info("Received chat message from Kafka for case ID '{}'", messageDto.getCaseId());
        
        try {
            // 1. Broadcast over WebSockets
            String destination = "/queue/messages-" + messageDto.getCaseId();
            log.info("Broadcasting message over WebSockets to destination '{}'", destination);
            messagingTemplate.convertAndSend(destination, messageDto);
            
            // 2. Persist to PostgreSQL database
            Case legalCase = caseRepository.findById(messageDto.getCaseId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Cannot persist message. Case not found with ID: " + messageDto.getCaseId()
                    ));

            Message entity = Message.builder()
                    .id(messageDto.getId()) // Preserve ID generated on client if present
                    .legalCase(legalCase)
                    .senderType(messageDto.getSenderType())
                    .content(messageDto.getContent())
                    .build();

            messageRepository.save(entity);
            log.info("Successfully persisted message entity to PostgreSQL");
            
        } catch (Exception e) {
            log.error("Error processing consumed Kafka message: ", e);
        }
    }
}
