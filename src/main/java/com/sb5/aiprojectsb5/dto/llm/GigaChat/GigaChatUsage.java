package com.sb5.aiprojectsb5.dto.llm.GigaChat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GigaChatUsage {
    @JsonProperty("prompt_tokens")
    private Integer promptTokens;
    @JsonProperty("completion_tokens")
    private Integer completionTokens;
    @JsonProperty("total_tokens")
    private Integer totalTokens;
    @JsonProperty("precached_prompt_tokens")
    private Integer precachedPromptTokens;
}
