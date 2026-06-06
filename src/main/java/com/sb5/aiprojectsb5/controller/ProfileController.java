package com.sb5.aiprojectsb5.controller;

import com.sb5.aiprojectsb5.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Tag(name = "Profile", description = "Профиль пользователя и статистика")
public class ProfileController {

    private final UserService userService;

    @Operation(
            summary = "Получить статистику пользователя",
            description = "Возвращает количество посещений, посещённых мест и баланс баллов"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статистика успешно получена"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{userId}/stats")
    public ResponseEntity<?> getUserStats(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserStats(userId));
    }
}