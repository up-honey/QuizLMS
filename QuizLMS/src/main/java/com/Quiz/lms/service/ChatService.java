//package com.Quiz.lms.service;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Service
//public class ChatService {
//
//    @Value("${anthropic.api.key}")
//    private String apiKey;
//
//    private final String ANTHROPIC_API_URL = "https://api.anthropic.com/v1/messages";
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    public String processMessage(String message) {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("x-api-key", apiKey);
//
//        String requestBody = String.format(
//                "{\"model\":\"claude-3-sonnet-20240229\",\"max_tokens\":1024,\"messages\":[{\"role\":\"user\",\"content\":\"%s\"}]}",
//                message.replace("\"", "\\\"")
//        );
//
//        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity(ANTHROPIC_API_URL, request, String.class);
//
//        try {
//            JsonNode rootNode = objectMapper.readTree(response.getBody());
//            String content = rootNode.path("content").path(0).path("text").asText();
//            return content;
//        } catch (Exception e) {
//            return "죄송해요, 응답을 처리하는 데 문제가 생겼어요.";
//        }
//    }
//}