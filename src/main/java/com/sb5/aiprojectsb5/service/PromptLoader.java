package com.sb5.aiprojectsb5.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Component
public class PromptLoader {

    public static final String SYSTEM_ROLE = """
            Ты — опытный педагог с 20-летним стажем. 
            Твоя речь живая, увлекательная, профессиональная.
            Ты всегда отвечаешь только в формате JSON, без лишних пояснений.
            """;

    public String loadPrompt(String path) {
        try {
            var resource = new ClassPathResource("prompts/" + path);
            return Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить промпт: " + path, e);
        }
    }

    public String formatScenarioStructure(String topic, String format, int duration,
                                          String wishes, int grade, String materials) {
        String template = loadPrompt("scenario-structure.txt");
        return String.format(template, topic, format, duration, wishes, grade, materials);
    }

    public String formatTeacherSpeech(int grade, String topic, String stageName,
                                      String activity, int time, int stageNum, int totalStages) {
        String template = loadPrompt("teacher-speech.txt");
        return String.format(template, grade, topic, stageName, activity, time, stageNum, totalStages);
    }

    public String formatSystemRole(String subject, int grade) {
        String template = loadPrompt("system-role.txt");
        return String.format(template, subject, grade);
    }

    public String getSystemRole() {
        return SYSTEM_ROLE;
    }
}
