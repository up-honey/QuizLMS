package com.Quiz.lms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    @Value("${flask.server.url}")
    private String flaskServerUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private boolean isRaccoon = false;

    public String processMessage(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", message);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(flaskServerUrl + "/chatbot", request, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String gptResponse = (String) response.getBody().get("answer");
                return formatResponse(gptResponse, message);
            } else {
                logger.error("Unexpected response from Flask server: {}", response);
                return "죄송합니다, 응답을 처리하는 데 문제가 생겼어요" + (isRaccoon ? "너굴." : "바오.");
            }
        } catch (Exception e) {
            logger.error("Error calling Flask server", e);
            return "서버 호출 중 오류가 발생했습니다" + (isRaccoon ? "너굴: " : "바오: ") + e.getMessage();
        }
    }

    private String formatResponse(String response, String userMessage) {
        if (userMessage.contains("나는 너가 판다가 아닌것을 안다.")) {
            isRaccoon = true;
            return "들켰다 나는 사실 너굴맨이야 너굴";
        }

        if (isRaccoon) {
            if (!response.endsWith("너굴")) {
                response += "너굴";
            }
        } else {
            if (!response.endsWith("바오")) {
                response += "바오";
            }
        }

        if (response.length() > 120) {
            response = response.substring(0, 117) + "...";
        }

        return response;
    }
}