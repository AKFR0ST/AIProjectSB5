package com.sb5.aiprojectsb5.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ГЭС-2 AI Компаньон API")
                        .version("1.0.0")
                        .description("""
                    API для персонализированного ИИ-агента ГЭС-2.
                    
                    **Основные возможности:**
                    * Авторизация по QR-коду бейджа
                    * Трекинг посещений локаций
                    * Начисление баллов лояльности
                    * Персональные AI-рекомендации маршрутов
                    * Достижения и геймификация
                    
                    **Технологии:** Spring Boot 3, GigaChat AI, PostgreSQL
                    """)
                        .contact(new Contact()
                                .name("ГЭС-2 Команда")
                                .email("support@ges2.ru"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8088")
                                .description("Локальный сервер"),
                        new Server()
                                .url("https://api.ges2.ru")
                                .description("Production сервер")
                ))
                .tags(List.of(
                        new Tag().name("Auth").description("Аутентификация и управление сессиями"),
                        new Tag().name("Scan").description("Сканирование QR-кодов и трекинг"),
                        new Tag().name("Profile").description("Профиль пользователя и статистика"),
                        new Tag().name("Routes").description("AI-маршруты и рекомендации"),
                        new Tag().name("Achievements").description("Достижения и геймификация"),
                        new Tag().name("Places").description("Информация о локациях ГЭС-2"),
                        new Tag().name("Events").description("Мероприятия и расписание")
                ));
    }
}
