package com.sb5.aiprojectsb5.dto.llm;

import lombok.Data;

@Data
public class LlmRequest {
    private String role;
    private String prompt;
}
