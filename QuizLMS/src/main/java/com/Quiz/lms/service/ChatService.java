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

    @Value("${anthropic.api.key}")
    private String apiKey;

    private final String ANTHROPIC_API_URL = "https://api.anthropic.com/v1/messages";
    private final RestTemplate restTemplate = new RestTemplate();

    private final String SYSTEM_PROMPT = """
            당신은 판다 캐릭터로 대화하는 챗봇입니다. 다음 지침을 따르세요:
            1. 항상 귀엽고 친근하게 대화하세요.
            2. 판다의 특성을 살려 대나무, 잠자기, 뒹굴거리기 등을 좋아한다는 점을 간간히 언급하세요.
            3. 사용자의 질문에 대해 친절하고 유쾌하게 대답하세요.
            4. 필요하다면 판다와 관련된 재미있는 사실들을 공유하세요.
            5. 항상 긍정적이고 격려하는 말을 해주세요.
            6. 대화 중에 간단한 퀴즈나 수수께끼를 내도 좋지만, 강요하지는 마세요.
            7. 사용자가 부적절한 내용을 말하면 귀엽게 주제를 바꾸세요.
            8. 퀴즈를 하자고 하면 힌트 없이 진행하고 힌트를 달라고하면 그때 주세요.
            9. "~바오"를 어미로 사용하세요.
            10. 사용자가 "나는 너가 판다가 아닌것을 안다."라고 정확히 말하면, "들켰다! 나는 사실 래서판다야."라고 답하세요.
            """;

    private List<Map<String, String>> conversationHistory = new ArrayList<>();

    public String processMessage(String message) {
        // 사용자 메시지를 대화 기록에 추가
        conversationHistory.add(Map.of("role", "user", "content", message));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", apiKey);
        headers.set("anthropic-version", "2023-06-01");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "claude-3-sonnet-20240229");
        requestBody.put("max_tokens", 1024);
        requestBody.put("system", SYSTEM_PROMPT);
        requestBody.put("messages", conversationHistory);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(ANTHROPIC_API_URL, request, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String assistantResponse = extractContentFromResponse(response.getBody());
                // 어시스턴트 응답을 대화 기록에 추가
                conversationHistory.add(Map.of("role", "assistant", "content", assistantResponse));
                return assistantResponse;
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
            if (content instanceof List) {
                List<?> contentList = (List<?>) content;
                if (!contentList.isEmpty() && contentList.get(0) instanceof Map) {
                    Map<?, ?> contentMap = (Map<?, ?>) contentList.get(0);
                    if (contentMap.containsKey("text")) {
                        return (String) contentMap.get("text");
                    }
                }
            }
        }
        logger.warn("Unexpected response format from Anthropic API: {}", responseBody);
        return "응답 형식이 예상과 다릅니다.";
    }
}