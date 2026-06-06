package com.sb5.aiprojectsb5.client;

import com.sb5.aiprojectsb5.dto.llm.GigaChat.GigaChatBaseRequest;
import com.sb5.aiprojectsb5.dto.llm.GigaChat.GigaChatBaseResponse;
import com.sb5.aiprojectsb5.dto.llm.GigaChat.GigaChatRequestMessage;
import com.sb5.aiprojectsb5.dto.llm.GigaChat.GigaChatResponseToken;
import com.sb5.aiprojectsb5.dto.llm.LlmRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.UUID;

import static com.sb5.aiprojectsb5.constants.GigaChatConstants.*;
import static com.sb5.aiprojectsb5.entity.llm.GigaChatModels.GIGACHAT_2_MAX;
import static com.sb5.aiprojectsb5.entity.llm.GigaChatModels.GIGACHAT_2_PRO;


@Component
public class GigaChatClient {

    private final String authKey;
    private final String authUrl;
    private final String baseUrl;

    private RestClient authRestClient;
    private RestClient baseRestClient;
    private String token;

    public GigaChatClient(
            @Value("${gigachat.auth.url}") String authUrl,
            @Value("${gigachat.authorization.key}") String authKey,
            @Value("${gigachat.base.url}") String baseUrl

    ) {
        this.authKey = authKey;
        this.authUrl = authUrl;
        this.baseUrl = baseUrl;

        updateAuthClient();
        updateBaseClient();
    }

    private void updateAuthClient() {
        authRestClient = RestClient.builder()
                .baseUrl(authUrl)
                .defaultHeader(CONTENT_TYPE, APPLICATION_X_WWW_FORM_URLENCODED)
                .defaultHeader(RQ_UID, UUID.randomUUID().toString())
                .defaultHeader(AUTHORIZATION, BEARER + authKey)
                .build();
    }

    private void updateBaseClient() {

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(30));   // 30 секунд на подключение
        factory.setReadTimeout(Duration.ofSeconds(120));

        baseRestClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(factory)
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON)
                .defaultHeader(ACCEPT, APPLICATION_JSON)
                .defaultHeader(AUTHORIZATION, BEARER + token)
                .build();
    }

    private GigaChatBaseResponse executeWithRetryOnUnauthenticated(GigaChatBaseRequest requestChat) {
        try {
            return baseRestClient.post()
                    .body(requestChat)
                    .retrieve()
                    .body(GigaChatBaseResponse.class);
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                updateToken();
                updateBaseClient();
                return baseRestClient.post()
                        .body(requestChat)
                        .retrieve()
                        .body(GigaChatBaseResponse.class);
            } else {
                throw e;
            }
        }
    }

    public String gigaChatTextToTextRequest(LlmRequest llmRequest){
        return gigaChatTextToTextRequestWithAttachments(llmRequest);
    }

    private String gigaChatTextToTextRequestWithAttachments(LlmRequest llmRequest) {

        ArrayList<GigaChatRequestMessage> messages = new ArrayList<>();

        if (!llmRequest.getRole().isEmpty()) {
            GigaChatRequestMessage roleMessage = GigaChatRequestMessage.builder()
                    .role(SYSTEM)
                    .content(llmRequest.getRole())
                    .build();
            messages.add(roleMessage);
        }

        GigaChatRequestMessage textMessage = GigaChatRequestMessage.builder()
                .role(USER)
                .content(llmRequest.getPrompt())
                .build();
        messages.add(textMessage);

        GigaChatBaseRequest requestChat = GigaChatBaseRequest.builder()
                .model(GIGACHAT_2_PRO.getTitle())  //  TODO брать из конфига
                .stream(false)
                .updateInterval(0)
                .messages(messages)
                .build();

        GigaChatBaseResponse gigaChatBaseResponse = executeWithRetryOnUnauthenticated(requestChat);

        return gigaChatBaseResponse.getGigaChatChoices().getFirst().getRequestMessage().getContent();
    }

    private void updateToken() {
        GigaChatResponseToken result = authRestClient.post().body(SCOPE_GIGACHAT_API_PERS).retrieve().body(GigaChatResponseToken.class);

        assert result != null;
        token = result.getToken();
    }
}
