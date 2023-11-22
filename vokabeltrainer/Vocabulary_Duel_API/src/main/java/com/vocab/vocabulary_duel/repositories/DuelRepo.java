package com.vocab.vocabulary_duel.repositories;

import com.vocab.vocabulary_duel.entities.Duel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DuelRepo extends JpaRepository<Duel, Long> {
}
