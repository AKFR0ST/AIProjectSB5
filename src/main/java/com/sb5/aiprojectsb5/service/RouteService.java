package com.sb5.aiprojectsb5.service;

import com.sb5.aiprojectsb5.dto.SaveRouteRequest;
import com.sb5.aiprojectsb5.dto.RouteStepDto;
import com.sb5.aiprojectsb5.entity.SavedRoute;
import com.sb5.aiprojectsb5.entity.User;
import com.sb5.aiprojectsb5.entity.VisitSession;
import com.sb5.aiprojectsb5.repository.RouteRepository;
import com.sb5.aiprojectsb5.repository.UserRepository;
import com.sb5.aiprojectsb5.repository.VisitSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final UserRepository userRepository;
    private final VisitSessionRepository sessionRepository;

    @Transactional
    public SavedRoute saveRoute(SaveRouteRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        VisitSession session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new RuntimeException("Сессия не найдена"));

        // Формируем строку с маршрутом (простой текст)
        String routeData = request.getSteps().stream()
                .map(step -> String.format("%d. %s (%s)",
                        step.getOrderNumber(), step.getPlaceName(), step.getType()))
                .collect(Collectors.joining("\n"));

        SavedRoute savedRoute = SavedRoute.builder()
                .user(user)
                .session(session)
                .routeData(routeData)
                .aiRecommendation(request.getAiRecommendation())
                .createdAt(LocalDateTime.now())
                .isCompleted(false)
                .build();

        return routeRepository.save(savedRoute);
    }

    public List<SavedRoute> getUserRoutes(UUID userId) {
        return routeRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<SavedRoute> getActiveRoutes(UUID userId) {
        return routeRepository.findActiveRoutesByUser(userId);
    }

    @Transactional
    public void completeRoute(UUID routeId) {
        routeRepository.markAsCompleted(routeId, LocalDateTime.now());
    }

    public Map<String, Object> getRouteWithProgress(UUID routeId) {
        SavedRoute route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Маршрут не найден"));

        Map<String, Object> result = new HashMap<>();
        result.put("routeId", route.getId());
        result.put("routeData", route.getRouteData());
        result.put("aiRecommendation", route.getAiRecommendation());
        result.put("isCompleted", route.getIsCompleted());
        result.put("createdAt", route.getCreatedAt());

        return result;
    }
}