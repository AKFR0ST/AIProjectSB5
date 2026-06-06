package com.sb5.aiprojectsb5.service;

import com.sb5.aiprojectsb5.entity.*;
import com.sb5.aiprojectsb5.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScanService {

    private final VisitTrackRepository trackRepository;
    private final PlaceRepository placeRepository;
    private final UserPointsRepository pointsRepository;
    private final PointTransactionRepository transactionRepository;
    private final VisitSessionRepository sessionRepository;

    private static final int POINTS_PER_VISIT = 10;

    @Transactional
    public VisitTrack scanQR(String qrCode, UUID sessionId, Integer placeId) {
        // Загружаем сессию с пользователем
        VisitSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Сессия не найдена"));

        // Проверяем, не сканировали ли уже это место в текущей сессии
        if (trackRepository.existsBySessionIdAndPlaceId(sessionId, placeId)) {
            throw new RuntimeException("Это место уже отсканировано в текущей сессии");
        }

        Optional<Place> placeOpt = placeRepository.findById(placeId);
        if (placeOpt.isEmpty()) {
            throw new RuntimeException("Место не найдено");
        }

        VisitTrack track = VisitTrack.builder()
                .session(session)
                .place(placeOpt.get())
                .qrCode(qrCode)
                .scannedAt(LocalDateTime.now())
                .build();

        VisitTrack saved = trackRepository.save(track);

        // Начисляем баллы за посещение
        addPointsForVisit(saved, session);

        return saved;
    }

    private void addPointsForVisit(VisitTrack track, VisitSession session) {
        UUID userId = session.getUser().getId();

        pointsRepository.addPoints(userId, POINTS_PER_VISIT);

        PointTransaction transaction = PointTransaction.builder()
                .user(session.getUser())
                .amount(POINTS_PER_VISIT)
                .reason("visit")
                .referenceId(track.getId())
                .build();

        transactionRepository.save(transaction);
    }
}