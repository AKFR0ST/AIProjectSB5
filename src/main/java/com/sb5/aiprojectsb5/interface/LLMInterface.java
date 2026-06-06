package com.sb5.aiprojectsb5;

import com.sb5.aiprojectsb5.dto.llm.LlmRequest;
import com.sb5.aiprojectsb5.entity.llm.LLMServices;

public interface LLMInterface {
    String sendTextToTextRequest(LlmRequest llmRequest, LLMServices llmServices);
}
