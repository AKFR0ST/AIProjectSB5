package com.sb5.aiprojectsb5.controller;

import com.sb5.aiprojectsb5.LLMInterface;
import com.sb5.aiprojectsb5.dto.llm.LlmRequest;
import com.sb5.aiprojectsb5.entity.VisitTrack;
import com.sb5.aiprojectsb5.entity.llm.LLMServices;
import com.sb5.aiprojectsb5.repository.VisitTrackRepository;
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
        if (history.isEmpty()) {
            return "Ты гид ГЭС-2. У пользователя нет истории посещений. " +
                    "Порекомендуй популярный маршрут на 30 минут. Ответ должен быть кратким и дружелюбным. " +
                    "Дополнительные пожелания: " + preferences;
        }

        // Анализируем теги из истории
        String tags = history.stream()
                .map(t -> t.getPlace().getTags())
                .filter(Objects::nonNull)
                .flatMap(t -> Arrays.stream(t.split(",")))
                .distinct()
                .limit(5)
                .collect(Collectors.joining(", "));

        return "Ты гид ГЭС-2. Проанализируй историю посещений пользователя (теги: " + tags + "). " +
                "Порекомендуй персональный маршрут на основе его интересов. " +
                "Дополнительные пожелания: " + preferences +
                "Ответ должен быть кратким (3-5 предложений) и дружелюбным.";
    }
}
