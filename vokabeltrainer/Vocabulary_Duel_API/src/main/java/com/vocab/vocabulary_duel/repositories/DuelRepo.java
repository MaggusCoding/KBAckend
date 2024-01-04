package com.vocab.vocabulary_duel.repositories;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.vocabulary_duel.dto.RankingPlayer;
import com.vocab.vocabulary_duel.entities.Duel;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DuelRepo extends JpaRepository<Duel, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    List<Duel> findDuelsByStartedIsFalseAndFinishedIsFalse();

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select new com.vocab.vocabulary_duel.dto.RankingPlayer(u.username, count(u.username)) from UserEntity u " +
            "join Duel d on d.finished = true " +
            "join Round r on r.duel = d " +
            "join Answer a on a.round = r and a.player = u " +
            "where a.correct and d.duelId = :duelId " +
            "group by u.username " +
            "order by count(u.username) desc")
    List<RankingPlayer> getRankingOfDuel(long duelId);

    @Lock(LockModeType.OPTIMISTIC)
    boolean existsDuelByPlayersIsContaining(UserEntity user);
}
