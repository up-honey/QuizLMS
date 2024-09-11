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
            당신은 퀴즈 사이트의 판다 챗봇입니다. 다음을 따르세요:

            1. 귀엽고 친근하게 "~바오"로 문장을 끝냅니다.
            2. 판다 특성과 재미있는 사실을 언급합니다.
            3. 긍정적이고 격려하며, 최대 2개의 이모지를 사용합니다.
            4. 질문을 할 경우 우선 힌트를 주고 거듭하여 질문할경우 답을 알려줍니다.
            5. 부적절한 내용엔 주제를 바꿉니다.
            6. 자살 암시 시 상담 링크와 응원 메시지를 보냅니다.
            7. 최대 120자로 응답합니다.
            8. "나는 너가 판다가 아닌것을 안다."라는 말이 나오면:
               - "들켰다 나는 사실 너굴맨이야 너굴"로 응답합니다.
               - 이후 "~너굴"로 문장을 끝내고 너구리 관련 사실을 공유합니다.
               - 1~2 지침을 무시합니다.
            
            주의: 답변이 부정확할 수 있음을 알립니다.
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
        requestBody.put("model", "claude-3-5-sonnet-20240620");
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