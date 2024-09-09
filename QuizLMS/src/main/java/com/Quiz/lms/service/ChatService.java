package com.Quiz.lms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    @Value("${anthropic.api.key}")
    private String apiKey;

    private final String ANTHROPIC_API_URL = "https://api.anthropic.com/v1/messages";
    private final RestTemplate restTemplate = new RestTemplate();

    public String processMessage(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", apiKey);
        headers.set("anthropic-version", "2023-06-01");  // 이 부분을 추가했습니다

        Map<String, Object> requestBody = Map.of(
                "model", "claude-3-sonnet-20240229",
                "max_tokens", 1024,
                "messages", Collections.singletonList(Map.of("role", "user", "content", message))
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(ANTHROPIC_API_URL, request, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return extractContentFromResponse(response.getBody());
            } else {
                logger.error("Unexpected response from Anthropic API: {}", response);
                return "죄송합니다, 응답을 처리하는 데 문제가 생겼어요.";
            }
        } catch (Exception e) {
            logger.error("Error calling Anthropic API", e);
            return "API 호출 중 오류가 발생했습니다: " + e.getMessage();
        }
    }

    private String extractContentFromResponse(Map responseBody) {
        if (responseBody.containsKey("content")) {
            Object content = responseBody.get("content");
            if (content instanceof Iterable) {
                for (Object item : (Iterable) content) {
                    if (item instanceof Map && ((Map) item).containsKey("text")) {
                        return (String) ((Map) item).get("text");
                    }
                }
            }
        }
        logger.warn("Unexpected response format from Anthropic API: {}", responseBody);
        return "응답 형식이 예상과 다릅니다.";
    }
}