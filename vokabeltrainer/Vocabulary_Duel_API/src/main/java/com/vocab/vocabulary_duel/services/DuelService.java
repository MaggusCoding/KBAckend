package com.vocab.vocabulary_duel.services;

import com.management.user_management.entities.User;
import com.vocab.vocabulary_duel.entities.Duel;
import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.FlashcardList;

import java.util.List;

public interface DuelService {
    /**
     * Creates a new duel
     * @param duel Duel to be created
     * @return Duel created
     */
    Duel createDuel(Duel duel);

    /**
     * Lets the user join a duel
     * @param duelId Id of the duel to join
     * @param userId User id of the user joining the duel
     * @return True if the user joined the duel, false otherwise
     */
    Boolean joinDuel(Long duelId, Long userId);

    /**
     * Gets a duel by its id
     * @param id Id of the duel to retrieve
     * @return Duel retrieved
     */
    Duel getById(Long id);

    /**
     * Gets all duels in the database
     * @return List of all duels
     */
    List<Duel> getAll();

    /**
     * Calculates the winners for a duel by comparing the scores of all the users
     * @param duel Duel to calculate the winner for
     * @return List of Users that won the duel
     */
    List<User> calculateWinner(Duel duel);

    /**
     * Generate a list of 10 Flashcards for a duel
     * @param flashcardList flashcardList the user chose to play
     * @param duel current duel
     * @return List of flashcard to be played
     */
    List<Flashcard> generateFlashcardList(FlashcardList flashcardList, Duel duel);

}
