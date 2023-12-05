package com.vocab.vocabulary_duel.repositories;

import com.vocab.vocabulary_duel.entities.Duel;
import com.vocab.vocabulary_duel.entities.Round;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoundRepo extends JpaRepository<Round, Long> {

    Round findRoundByDuelAndActiveRoundTrue(Duel duel);

    Round findFirstByDuelAndSelectedAnswersEmpty(Duel duel);
}
