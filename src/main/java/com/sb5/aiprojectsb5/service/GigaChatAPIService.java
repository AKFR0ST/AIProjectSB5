package com.sb5.aiprojectsb5.service;

import com.sb5.aiprojectsb5.client.GigaChatClient;
import com.sb5.aiprojectsb5.dto.llm.LlmRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GigaChatAPIService {

    private final GigaChatClient gigaChatClient;

    public String textToTextRequest(LlmRequest llmRequest) {
        return gigaChatClient.gigaChatTextToTextRequest(llmRequest);
    }
}
