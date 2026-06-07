package com.sb5.aiprojectsb5.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
@Schema(description = "Запрос на сохранение маршрута")
public class SaveRouteRequest {
    @Schema(description = "ID пользователя")
    private UUID userId;

    @Schema(description = "ID сессии")
    private UUID sessionId;

    @Schema(description = "Текст рекомендации от AI")
    private String aiRecommendation;

    @Schema(description = "Список мест в маршруте")
    private List<RouteStepDto> steps;
}

