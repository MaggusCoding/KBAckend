package com.vocab.vocabulary_duel.repositories;

import com.vocab.vocabulary_duel.entities.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepo extends JpaRepository<Answer, Long> {
}
