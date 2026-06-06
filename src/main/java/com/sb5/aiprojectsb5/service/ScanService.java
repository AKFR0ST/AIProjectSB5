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

    private static final int POINTS_PER_VISIT = 10;

    @Transactional
    public VisitTrack scanQR(String qrCode, UUID sessionId, Integer placeId) {
        // Проверяем, не сканировали ли уже это место в текущей сессии
        if (trackRepository.existsBySessionIdAndPlaceId(sessionId, placeId)) {
            throw new RuntimeException("Это место уже отсканировано в текущей сессии");
        }

        Optional<Place> placeOpt = placeRepository.findById(placeId);
        if (placeOpt.isEmpty()) {
            throw new RuntimeException("Место не найдено");
        }

        VisitTrack track = VisitTrack.builder()
                .session(VisitSession.builder().id(sessionId).build())
                .place(placeOpt.get())
                .qrCode(qrCode)
                .scannedAt(LocalDateTime.now())
                .build();

        VisitTrack saved = trackRepository.save(track);

        // Начисляем баллы за посещение
        addPointsForVisit(saved);

        return saved;
    }

    private void addPointsForVisit(VisitTrack track) {
        UUID userId = track.getSession().getUser().getId();

        pointsRepository.addPoints(userId, POINTS_PER_VISIT);

        PointTransaction transaction = PointTransaction.builder()
                .user(User.builder().id(userId).build())
                .amount(POINTS_PER_VISIT)
                .reason("visit")
                .referenceId(track.getId())
                .build();

        transactionRepository.save(transaction);
    }
}