package com.sb5.aiprojectsb5.client;


import com.sb5.aiprojectsb5.dto.llm.CloudRu.CloudRuRequest;
import com.sb5.aiprojectsb5.dto.llm.CloudRu.CloudRuResponse;
import com.sb5.aiprojectsb5.dto.llm.LlmRequest;
import com.sb5.aiprojectsb5.entity.llm.CloudRuModels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Slf4j
@Component
public class CloudRuClient {

    String baseUrl;
    String authKey;
    private RestClient baseRestClient;

    public CloudRuClient(
            @Value("${cloudru.base.url}") String baseUrl,
            @Value("${cloudru.authorization.key}") String authKey) {
        this.baseUrl = baseUrl;
        this.authKey = authKey;
        updateBaseClient();
    }

    private void updateBaseClient() {

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(60000);  // 60 секунд
        factory.setReadTimeout(120000);     // 120 секунд

        baseRestClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Authorization", "Bearer " + authKey)
                .build();
    }

    public String cloudRuTextToTextRequest(LlmRequest llmRequest, CloudRuModels cloudRuModels) {
        java.util.ArrayList<CloudRuRequest.Message> messages = new java.util.ArrayList<>();

        if (llmRequest.getRole() != null && !llmRequest.getRole().isEmpty()) {
            CloudRuRequest.Message roleMessage = CloudRuRequest.Message.builder()
                    .role("system")
                    .content(llmRequest.getRole())
                    .build();
            messages.add(roleMessage);
        }

        CloudRuRequest.Message textMessage = CloudRuRequest.Message.builder()
                .role("user")
                .content(llmRequest.getPrompt())
                .build();
        messages.add(textMessage);

        CloudRuRequest ruRequest = CloudRuRequest.builder()
                .model("GigaChat/GigaChat-2-Max")
                .messages(messages)
                .temperature(0.5)
                .topP(0.95)
                .maxTokens(2500)
                .frequencyPenalty(0.5)
                .presencePenalty(0.0)
                .build();

        log.info("CloudRuRequest: model={}", ruRequest.getModel());

        CloudRuResponse cloudRuResponse = executeRequest(ruRequest);

        String content = cloudRuResponse.getChoices().getFirst().getMessage().getContent();

        // Очищаем ответ от маркеров JSON, если они есть
        content = content.replace("```json", "").replace("```", "").trim();

        log.info("CloudRuResponse received, length={}", content.length());
        return content;
    }

    private CloudRuResponse executeRequest(CloudRuRequest request) {
        return baseRestClient.post()
                .body(request)
                .retrieve()
                .body(CloudRuResponse.class);
    }
}
