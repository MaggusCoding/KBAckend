package com.vocab.vocabulary_duel_API.repositories;

import com.vocab.vocabulary_duel_API.entities.Duel;
import com.vocab.vocabulary_duel_API.entities.Round;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoundRepo extends JpaRepository<Round, Long> {

    Round findRoundByDuelAndActiveRoundTrue(Duel duel);

    Round findFirstByDuelAndSelectedAnswersEmpty(Duel duel);
}
