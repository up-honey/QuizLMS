
package com.Quiz.lms.controller;

import com.Quiz.lms.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatService chatService;

    @PostMapping
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        //logger.info("Received message: {}", message);

        if (message == null || message.trim().isEmpty()) {
            //logger.warn("Received empty message");
            return ResponseEntity.badRequest().body(Map.of("error", "메시지가 비어있습니다."));
        }

        try {
            String response = chatService.processMessage(message);
            //logger.info("Processed response: {}", response);
            return ResponseEntity.ok(Map.of("response", response));
        } catch (Exception e) {
            //logger.error("Error processing message", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "메시지 처리 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }
}
