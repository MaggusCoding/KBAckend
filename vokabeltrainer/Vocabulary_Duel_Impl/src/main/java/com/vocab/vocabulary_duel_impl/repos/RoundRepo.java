package com.vocab.vocabulary_duel_impl.repos;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.vocabulary_duel_API.entities.Duel;
import com.vocab.vocabulary_duel_API.entities.Round;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoundRepo extends JpaRepository<Round, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    Round findRoundByDuelAndActiveRoundTrue(Duel duel);

    @Lock(LockModeType.OPTIMISTIC)
    Round findFirstByDuelAndSelectedAnswersEmpty(Duel duel);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select r from Round r " +
            "left join Answer a on a.round = r and a.player = :user " +
            "where r.duel = :duel and a.player is null")
    List<Round> findRoundsByDuelAndNotPlayedByUser(Duel duel, UserEntity user);

}
