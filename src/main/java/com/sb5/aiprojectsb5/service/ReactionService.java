package com.sb5.aiprojectsb5.service;

import com.sb5.aiprojectsb5.entity.Reaction;
import com.sb5.aiprojectsb5.entity.VisitTrack;
import com.sb5.aiprojectsb5.entity.PointTransaction;
import com.sb5.aiprojectsb5.entity.User;
import com.sb5.aiprojectsb5.repository.ReactionRepository;
import com.sb5.aiprojectsb5.repository.VisitTrackRepository;
import com.sb5.aiprojectsb5.repository.UserPointsRepository;
import com.sb5.aiprojectsb5.repository.PointTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final ReactionRepository reactionRepository;
    private final VisitTrackRepository trackRepository;
    private final UserPointsRepository pointsRepository;
    private final PointTransactionRepository transactionRepository;

    private static final int POINTS_FOR_REACTION = 5;

    @Transactional
    public Reaction addReaction(Integer trackId, String reactionType, String comment) {
        // Находим трек посещения
        VisitTrack track = trackRepository.findById(trackId)
                .orElseThrow(() -> new RuntimeException("Трек не найден"));

        // Проверяем, есть ли уже реакция на этот трек
        if (reactionRepository.existsByTrackId(trackId)) {
            throw new RuntimeException("Реакция уже оставлена для этого посещения");
        }

        // Создаём реакцию
        Reaction reaction = Reaction.builder()
                .track(track)
                .reactionType(reactionType)
                .comment(comment)
                .build();

        Reaction saved = reactionRepository.save(reaction);

        // Начисляем бонусные баллы за отзыв
        addPointsForReaction(track);

        return saved;
    }

    private void addPointsForReaction(VisitTrack track) {
        User user = track.getSession().getUser();

        pointsRepository.addPoints(user.getId(), POINTS_FOR_REACTION);

        PointTransaction transaction = PointTransaction.builder()
                .user(user)
                .amount(POINTS_FOR_REACTION)
                .reason("feedback")
                .referenceId(track.getId())
                .build();

        transactionRepository.save(transaction);
    }

    public List<Reaction> getUserReactions(UUID userId) {
        return reactionRepository.findByTrackSessionUserId(userId);
    }
}
