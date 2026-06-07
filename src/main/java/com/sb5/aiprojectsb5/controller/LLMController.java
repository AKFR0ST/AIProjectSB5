package com.sb5.aiprojectsb5.controller;

import com.sb5.aiprojectsb5.LLMInterface;
import com.sb5.aiprojectsb5.dto.RouteStepDto;
import com.sb5.aiprojectsb5.dto.SaveRouteRequest;
import com.sb5.aiprojectsb5.dto.llm.LlmRequest;
import com.sb5.aiprojectsb5.entity.Place;
import com.sb5.aiprojectsb5.entity.SavedRoute;
import com.sb5.aiprojectsb5.entity.VisitTrack;
import com.sb5.aiprojectsb5.entity.llm.LLMServices;
import com.sb5.aiprojectsb5.repository.PlaceRepository;
import com.sb5.aiprojectsb5.repository.VisitSessionRepository;
import com.sb5.aiprojectsb5.repository.VisitTrackRepository;
import com.sb5.aiprojectsb5.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/llm")
@Tag(name = "LLM", description = "API для запросов к GigaChat")
@RequiredArgsConstructor
public class LLMController {
    private final LLMInterface llmInterface;
    private final VisitTrackRepository trackRepository;
    private final PlaceRepository placeRepository;
    private final RouteService routeService;
    private final VisitSessionRepository sessionRepository;

    @Value("${general.llm.default}")
    private LLMServices llmDefault;

    @Operation(summary = "Отправить текстовый запрос к GigaChat")
    @PostMapping("/generate")
    public String llmRequest(@RequestBody LlmRequest request) {
        return llmInterface.sendTextToTextRequest(request, llmDefault);
    }

    @PostMapping("/recommend-route")
    public ResponseEntity<?> recommendRoute(@RequestBody Map<String, Object> request) {
        try {
            String userId = (String) request.get("userId");
            String preferences = (String) request.getOrDefault("preferences", "");

            // Получаем историю пользователя
            List<VisitTrack> history = trackRepository.findAllByUserId(UUID.fromString(userId));

            // Формируем промпт с контекстом
            String prompt = buildContextPrompt(history, preferences);

            LlmRequest llmRequest = new LlmRequest();
            llmRequest.setRole("assistant");
            llmRequest.setPrompt(prompt);

            String aiResponse = llmInterface.sendTextToTextRequest(llmRequest, llmDefault);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "route", aiResponse
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }

    private String buildContextPrompt(List<VisitTrack> history, String preferences) {
        // Получаем все доступные места из базы
        List<Place> allPlaces = placeRepository.findAll();

        // Формируем список мест для AI
        String placesList = allPlaces.stream()
                .map(p -> String.format("ID: %d, Название: %s, Тип: %s, Теги: %s",
                        p.getId(), p.getName(), p.getType(), p.getTags()))
                .collect(Collectors.joining("\n"));

        if (history.isEmpty()) {
            return String.format("""
            Ты гид ГЭС-2. У пользователя нет истории посещений.
            
            Вот список реальных мест в ГЭС-2:
            %s
            
            Порекомендуй 3 места из этого списка (ТОЛЬКО из списка выше!).
            Ответ должен быть кратким, дружелюбным и содержать конкретные названия мест.
            Дополнительные пожелания пользователя: %s
            """, placesList, preferences);
        }

        // Анализируем теги из истории
        String tags = history.stream()
                .map(t -> t.getPlace().getTags())
                .filter(Objects::nonNull)
                .flatMap(t -> Arrays.stream(t.split(",")))
                .map(String::trim)
                .distinct()
                .limit(5)
                .collect(Collectors.joining(", "));

        // Получаем ID уже посещённых мест
        List<Integer> visitedIds = history.stream()
                .map(t -> t.getPlace().getId())
                .toList();

        // Фильтруем непосещённые места
        String availablePlaces = allPlaces.stream()
                .filter(p -> !visitedIds.contains(p.getId()))
                .map(p -> String.format("ID: %d, Название: %s, Тип: %s, Теги: %s",
                        p.getId(), p.getName(), p.getType(), p.getTags()))
                .collect(Collectors.joining("\n"));

        return String.format("""
        Ты гид ГЭС-2. Проанализируй историю посещений пользователя.
        
        История пользователя (интересы по тегам): %s
        
        Вот список доступных мест в ГЭС-2 (ТОЛЬКО из этого списка!):
        %s
        
        Порекомендуй 3 места из списка выше, которые пользователь ещё не посещал.
        Не выдумывай места, используй ТОЛЬКО названия из списка.
        Ответ должен быть кратким (3-5 предложений) и дружелюбным.
        
        Дополнительные пожелания: %s
        """, tags, availablePlaces, preferences);
    }

    // Метод для получения и сохранения маршрута
    @PostMapping("/recommend-route-and-save")
    public ResponseEntity<?> recommendRouteAndSave(@RequestBody Map<String, Object> request) {
        try {
            String userId = (String) request.get("userId");
            String sessionId = (String) request.get("sessionId");
            String preferences = (String) request.getOrDefault("preferences", "");

            // Получаем историю пользователя
            List<VisitTrack> history = trackRepository.findAllByUserId(UUID.fromString(userId));

            // Формируем промпт с контекстом
            String prompt = buildContextPrompt(history, preferences);

            LlmRequest llmRequest = new LlmRequest();
            llmRequest.setRole("assistant");
            llmRequest.setPrompt(prompt);

            String aiResponse = llmInterface.sendTextToTextRequest(llmRequest, llmDefault);

            // Парсим рекомендации из AI ответа и создаём шаги маршрута
            List<Map<String, Object>> steps = parseAiResponseToSteps(aiResponse);

            // Сохраняем маршрут
            SaveRouteRequest saveRequest = new SaveRouteRequest();
            saveRequest.setUserId(UUID.fromString(userId));
            saveRequest.setSessionId(UUID.fromString(sessionId));
            saveRequest.setAiRecommendation(aiResponse);
            // Преобразуем steps в RouteStepDto
            List<RouteStepDto> stepDtos = steps.stream().map(step -> {
                RouteStepDto dto = new RouteStepDto();
                dto.setPlaceId((Integer) step.get("placeId"));
                dto.setPlaceName((String) step.get("placeName"));
                dto.setType((String) step.get("type"));
                dto.setOrderNumber((Integer) step.get("orderNumber"));
                return dto;
            }).collect(Collectors.toList());
            saveRequest.setSteps(stepDtos);

            SavedRoute savedRoute = routeService.saveRoute(saveRequest);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "route", aiResponse,
                    "savedRouteId", savedRoute.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }

    private List<Map<String, Object>> parseAiResponseToSteps(String aiResponse) {
        // Простой парсинг - можно улучшить, попросив AI возвращать JSON
        List<Map<String, Object>> steps = new ArrayList<>();

        // Пример: извлекаем ID мест из текста (временно)
        // В идеале - попросить AI вернуть JSON с ID мест
        List<Place> allPlaces = placeRepository.findAll();

        // Простой алгоритм: рекомендует первые 3 непосещённых места
        int order = 1;
        for (Place place : allPlaces.stream().limit(3).collect(Collectors.toList())) {
            Map<String, Object> step = new HashMap<>();
            step.put("placeId", place.getId());
            step.put("placeName", place.getName());
            step.put("type", place.getType());
            step.put("orderNumber", order++);
            steps.add(step);
        }

        return steps;
    }

    @PostMapping("/save-route")
    public ResponseEntity<?> saveRouteManually(@RequestBody SaveRouteRequest request) {
        try {
            SavedRoute savedRoute = routeService.saveRoute(request);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "savedRouteId", savedRoute.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }

}
