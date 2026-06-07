package com.sb5.aiprojectsb5.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ReactionRequest {
    @Schema(description = "ID трека посещения", example = "123")
    private Integer trackId;

    @Schema(description = "Тип реакции", example = "positive", allowableValues = {"positive", "neutral", "negative"})
    private String reactionType;

    @Schema(description = "Комментарий (опционально)", example = "Очень понравилась инсталляция!")
    private String comment;
}
