package com.vocab.vocabulary_duel.repositories;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.vocabulary_duel.dto.RankingPlayer;
import com.vocab.vocabulary_duel.entities.Duel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DuelRepo extends JpaRepository<Duel, Long> {

    List<Duel> findDuelsByStartedIsFalseAndFinishedIsFalse();

    @Query("select new com.vocab.vocabulary_duel.dto.RankingPlayer(u.username, count(u.username)) from UserEntity u " +
            "join Duel d on d.finished = true " +
            "join Round r on r.duel = d " +
            "join Answer a on a.round = r and a.player = u " +
            "where a.correct and d.duelId = :duelId " +
            "group by u.username " +
            "order by count(u.username) desc")
    List<RankingPlayer> getRankingOfDuel(long duelId);

    boolean existsDuelByPlayersIsContaining(UserEntity user);
}
