package com.sb5.aiprojectsb5;

import com.sb5.aiprojectsb5.dto.llm.LlmRequest;
import com.sb5.aiprojectsb5.entity.llm.LLMServices;
import com.sb5.aiprojectsb5.service.CloudRuAPIService;
import com.sb5.aiprojectsb5.service.GigaChatAPIService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.sb5.aiprojectsb5.entity.llm.CloudRuModels.CLOUD_GIGA_CHAT_2;

@Component
@AllArgsConstructor
public class LLMInterfaceImpl implements com.sb5.aiprojectsb5.LLMInterface {

    private static final String MODEL_INCORRECT = "Некорректная модель";
    GigaChatAPIService gigaChatAPIService;
    CloudRuAPIService cloudRuAPIService;

    @Override
    public String sendTextToTextRequest(LlmRequest llmRequest, LLMServices llmServices) {
        return switch (llmServices) {
            case GIGA_CHAT -> gigaChatAPIService.textToTextRequest(llmRequest);
            case CLOUD_GIGA_CHAT_2 -> cloudRuAPIService.textToTextRequest(llmRequest, CLOUD_GIGA_CHAT_2);
            default -> MODEL_INCORRECT;
        };
    }

}
