package com.sb5.aiprojectsb5.controller;

import com.sb5.aiprojectsb5.entity.VisitSession;
import com.sb5.aiprojectsb5.entity.VisitTrack;
import com.sb5.aiprojectsb5.service.ScanService;
import com.sb5.aiprojectsb5.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Аутентификация и управление сессиями")
public class AuthController {

    private final UserService userService;

    @Operation(
            summary = "Вход по QR-коду",
            description = "Сканирование QR-кода с бейджа. Если пользователь новый - создаётся, если существующий - открывается сессия."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный вход"),
            @ApiResponse(responseCode = "400", description = "Неверный QR-код", content = @Content)
    })
    @PostMapping("/auth/scan-qr")
    public ResponseEntity<?> loginByQR(@RequestBody Map<String, String> request) {
        String qrCode = request.get("qrCode");
        String phone = request.getOrDefault("phone", null);

        var user = userService.registerOrGetUser(qrCode, phone);
        var session = userService.startSession(user.getId());

        return ResponseEntity.ok(Map.of(
                "userId", user.getId(),
                "sessionId", session.getId(),
                "isNew", user.getCreatedAt().equals(user.getLastVisit()),
                "pointsBalance", userService.getUserStats(user.getId()).get("pointsBalance")
        ));
    }
}