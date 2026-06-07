package com.sb5.aiprojectsb5.controller;

import com.sb5.aiprojectsb5.dto.ReactionRequest;
import com.sb5.aiprojectsb5.entity.Reaction;
import com.sb5.aiprojectsb5.entity.VisitTrack;
import com.sb5.aiprojectsb5.service.ScanService;
import com.sb5.aiprojectsb5.service.ReactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private final ReactionService reactionService;  // Добавить

    @Operation(summary = "Сканировать QR-код локации")
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

    @Operation(summary = "Оставить реакцию на посещённое место")
    @PostMapping("/reaction")
    public ResponseEntity<?> addReaction(@RequestBody ReactionRequest request) {
        try {
            Reaction reaction = reactionService.addReaction(
                    request.getTrackId(),
                    request.getReactionType(),
                    request.getComment()
            );

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "reactionId", reaction.getId(),
                    "message", "Спасибо за отзыв! +5 бонусов"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }
}