package com.vocab.vocabulary_duel_impl.repos;

import com.vocab.vocabulary_duel_API.entities.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepo extends JpaRepository<Answer, Long> {
}
