package com.vocab.vocabulary_duel_API.repositories;

import com.vocab.vocabulary_duel_API.entities.Duel;
import com.vocab.vocabulary_duel_API.entities.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;

public interface RoundRepo extends JpaRepository<Round, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    Round findRoundByDuelAndActiveRoundTrue(Duel duel);

    @Lock(LockModeType.OPTIMISTIC)
    Round findFirstByDuelAndSelectedAnswersEmpty(Duel duel);
}
