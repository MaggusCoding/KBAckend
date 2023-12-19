package com.vocab.vocabulary_duel.services;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.vocabulary_duel.entities.Duel;

import java.util.List;
import java.util.Optional;

public interface DuelService {
    /**
     * Creates a new duel
     * @param userId User id of the user creating the duel
     * @param flashcardListId   Id of the flashcard list to use for the duel
     * @return Duel created
     */
    Duel createDuel(Long userId, Long flashcardListId);

    /**
     * Lets the user join a duel
     * @param duelId Id of the duel to join
     * @param userId User id of the user joining the duel
     * @return True if the user joined the duel, false otherwise
     */
    Boolean joinDuel(long duelId, long userId);

    /**
     * Gets a duel by its id
     *
     * @param id Id of the duel to retrieve
     * @return Duel retrieved
     */
    Optional<Duel> getById(Long id);

    /**
     * Gets all duels in the database
     * @return List of all duels
     */
    List<Duel> getAll();

    /**
     * Calculates the winners for a duel by comparing the amount of correct answers of all the users
     * @param duelId DuelId to calculate the winner for
     * @return List of Users that won the duel
     */
    List<UserEntity> calculateWinner(Long duelId);

    boolean startDuel(Long duelId);

    /**
     * Saves the selected Answer and determines if it is correct.
     * @param selectedAnswer selected Answer
     * @param duelId current duel
     * @param playerId current player
     */
    void saveSelectedAnswer(String selectedAnswer, Long duelId, Long playerId);

    /**
     * deactivates current round and activates the next round. If all rounds were played the duel is updated to finished.
     * @param duelId of current duel
     */
    void activateNextRound(Long duelId);

    /**
     * returns not played and not finished duels.
     * @return duels
     */
    List<Duel> duelsToJoin();

    /**
     * returns duels that are not started and not finished and the user has joined.
     * @param userId user who wants to start a duel
     * @return duels
     */
    List<Duel> duelsToStart(Long userId);

    /**
     * TODO javadoc
     * @param duelStart Id of duel
     * @return List<String> content of flashcard
     */
    List<String> playRound(Long duelStart);

    /**
     * Generate 10 rounds of a newly created duel.
     * @param duelId Id of duel
     */
    void generateRounds(Long duelId);

    /**
     * Deletes a duel.
     *
     * @param duelId Id of duel
     * @return if deletion succeeded
     */
    boolean deleteDuel(Long duelId);
}
