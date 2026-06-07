package com.sb5.aiprojectsb5.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Шаг маршрута")
public class RouteStepDto {
    private Integer placeId;
    private String placeName;
    private String type;
    private Integer orderNumber;
}
