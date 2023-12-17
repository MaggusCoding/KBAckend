package com.vocab.vocabulary_duel.repositories;

import com.vocab.vocabulary_duel.entities.Duel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DuelRepo extends JpaRepository<Duel, Long> {

    List<Duel> findDuelsByStartedIsFalseAndFinishedIsFalse();

}
