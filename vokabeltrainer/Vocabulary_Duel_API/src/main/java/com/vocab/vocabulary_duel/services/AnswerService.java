package com.vocab.vocabulary_duel.services;

import com.vocab.vocabulary_duel.entities.Answer;
import java.util.List;

public interface AnswerService {
    /**
     * Create a new answer and save it in the database
     * @param answer Answer to be created
     * @return Answer created
     */
    Answer createAnswer(Answer answer);

    /**
     * Gets an answer by its id
     * @param id Id of the answer
     * @return Answer with the given id
     */
    Answer getById(Long id);

    /**
     * Gets all selected answers of a round
     * @param roundId Id of the round which the answers belong to
     * @return List of all the answers
     */
    List<Answer> getAllAnswersByRound(Long roundId);

}
