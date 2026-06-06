package com.sb5.aiprojectsb5.dto.llm.GigaChat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class GigaChatResponseToken {
    @JsonProperty("access_token")
    private String token;
    @JsonProperty("expires_at")
    private Date expiration;

}
