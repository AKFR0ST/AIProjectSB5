package com.sb5.aiprojectsb5.service;

import com.sb5.aiprojectsb5.entity.*;
import com.sb5.aiprojectsb5.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final VisitSessionRepository sessionRepository;
    private final UserPointsRepository pointsRepository;

    @Transactional
    public User registerOrGetUser(String badgeId, String phone) {
        return userRepository.findByBadgeId(badgeId)
                .orElseGet(() -> {
                    User user = User.builder()
                            .badgeId(badgeId)
                            .phone(phone)
                            .createdAt(LocalDateTime.now())
                            .build();
                    User saved = userRepository.save(user);

                    // Инициализируем баллы
                    UserPoints points = UserPoints.builder()
                            .userId(saved.getId())
                            .balance(0)
                            .build();
                    pointsRepository.save(points);

                    return saved;
                });
    }

    @Transactional
    public VisitSession startSession(UUID userId) {
        // Закрываем старую сессию, если есть
        sessionRepository.findFirstByUserIdAndEndedAtIsNullOrderByStartedAtDesc(userId)
                .ifPresent(session -> {
                    session.setEndedAt(LocalDateTime.now());
                    session.setDurationMinutes((int) java.time.Duration.between(
                            session.getStartedAt(), LocalDateTime.now()).toMinutes());
                    sessionRepository.save(session);
                });

        VisitSession session = VisitSession.builder()
                .user(User.builder().id(userId).build())
                .startedAt(LocalDateTime.now())
                .build();

        return sessionRepository.save(session);
    }

    public Map<String, Object> getUserStats(UUID userId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalVisits", userRepository.countVisitsByUserId(userId));
        stats.put("totalPlacesVisited", sessionRepository.countDistinctVisitedPlacesByUser(userId));

        pointsRepository.findByUserId(userId).ifPresent(points ->
                stats.put("pointsBalance", points.getBalance()));

        return stats;
    }
}
