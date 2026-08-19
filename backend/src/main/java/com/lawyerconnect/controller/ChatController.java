package com.lawyerconnect.controller;

import com.lawyerconnect.dto.MessageDto;
import com.lawyerconnect.messaging.MessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final MessageProducer messageProducer;

    /**
     * WebSockets API: Ingests chat payloads sent from clients.
     * Listens at STOMP destination: "/app/chat.send".
     * Routes the DTO payload directly to the Kafka cluster for streaming processing.
     */
    @MessageMapping("/chat.send")
    public void handleWebSocketChatMessage(@Payload MessageDto messageDto) {
        log.info("Received WebSocket chat message from client. Forwarding to Kafka broker for case ID: '{}'", 
                messageDto.getCaseId());
                
        messageProducer.sendMessage(messageDto);
    }
}
