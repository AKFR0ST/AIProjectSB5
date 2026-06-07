package com.sb5.aiprojectsb5.controller;

import com.sb5.aiprojectsb5.entity.SavedRoute;
import com.sb5.aiprojectsb5.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
@Tag(name = "Routes", description = "Управление сохранёнными маршрутами")
public class RouteController {

    private final RouteService routeService;

    @Operation(summary = "Получить все маршруты пользователя")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SavedRoute>> getUserRoutes(@PathVariable UUID userId) {
        return ResponseEntity.ok(routeService.getUserRoutes(userId));
    }

    @Operation(summary = "Получить активные маршруты пользователя")
    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<SavedRoute>> getActiveRoutes(@PathVariable UUID userId) {
        return ResponseEntity.ok(routeService.getActiveRoutes(userId));
    }

    @Operation(summary = "Получить маршрут с прогрессом")
    @GetMapping("/{routeId}")
    public ResponseEntity<Map<String, Object>> getRouteWithProgress(@PathVariable UUID routeId) {
        return ResponseEntity.ok(routeService.getRouteWithProgress(routeId));
    }

    @Operation(summary = "Отметить маршрут как пройденный")
    @PostMapping("/{routeId}/complete")
    public ResponseEntity<?> completeRoute(@PathVariable UUID routeId) {
        routeService.completeRoute(routeId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Маршрут отмечен как пройденный"
        ));
    }
}
