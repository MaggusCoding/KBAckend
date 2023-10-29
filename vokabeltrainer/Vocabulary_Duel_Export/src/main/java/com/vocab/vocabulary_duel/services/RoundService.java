package com.vocab.vocabulary_duel.services;

import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_duel.entities.Round;

import java.util.List;

public interface RoundService {
    /**
     * Creates a new round
     * @param round Round to be created
     * @return Round created
     */
    Round createRound(Round round);

    /**
     * Generates a list of wrong answers for a round
     * @param round Round to generate wrong answers for
     * @return List of wrong answers
     */
    List<String> generateWrongAnswers(Round round);

    /**
     * Gets a random flashcard from a flashcard list
     * @param id Id of the flashcard list to get a random flashcard from
     * @return Random flashcard from the flashcard list
     */
    Flashcard getRandomFlashcardFromFlashcardList(Long id);

    /**
     * Gets a round by its id
     * @param id Id of the round to retrieve
     * @return Round retrieved
     */
    Round getById(Long id);

    /**
     * Gets all rounds of a duel
     * @param duelId Id of the duel which the rounds belong to
     * @return List of all rounds
     */
    List<Round> getAllRoundsByDuel(Long duelId);

}
