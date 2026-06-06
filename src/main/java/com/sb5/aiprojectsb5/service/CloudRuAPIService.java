package com.sb5.aiprojectsb5.service;

import com.sb5.aiprojectsb5.client.CloudRuClient;
import com.sb5.aiprojectsb5.dto.llm.LlmRequest;
import com.sb5.aiprojectsb5.entity.llm.CloudRuModels;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CloudRuAPIService {

    private final CloudRuClient cloudRuClient;

    public String textToTextRequest(LlmRequest llmRequest, CloudRuModels cloudRuModels) {
        return cloudRuClient.cloudRuTextToTextRequest(llmRequest, cloudRuModels);
    }
}
