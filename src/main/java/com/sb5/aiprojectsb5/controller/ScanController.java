package com.sb5.aiprojectsb5.controller;

import com.sb5.aiprojectsb5.entity.VisitTrack;
import com.sb5.aiprojectsb5.service.ScanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/scan")
@RequiredArgsConstructor
@Tag(name = "Scan", description = "Сканирование QR-кодов и трекинг посещений")
public class ScanController {

    private final ScanService scanService;

    @Operation(
            summary = "Сканировать QR-код локации",
            description = "При сканировании QR-кода у экспоната/локации начисляются баллы и сохраняется цифровой след"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сканирование успешно"),
            @ApiResponse(responseCode = "400", description = "Ошибка сканирования (уже было или место не найдено)")
    })
    @PostMapping
    public ResponseEntity<?> scanPlace(@RequestBody Map<String, Object> request) {
        String qrCode = (String) request.get("qrCode");
        String sessionIdStr = (String) request.get("sessionId");
        Integer placeId = (Integer) request.get("placeId");

        UUID sessionId = UUID.fromString(sessionIdStr);

        try {
            VisitTrack track = scanService.scanQR(qrCode, sessionId, placeId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "trackId", track.getId(),
                    "placeName", track.getPlace().getName(),
                    "pointsEarned", 10
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }
}