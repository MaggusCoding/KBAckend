package com.vocab.vocabulary_duel_API.services;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.vocabulary_duel_API.entities.Duel;
import com.vocab.vocabulary_duel_API.entities.Round;

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

    /**
     * Starts the duel so that nobody can join anymore and sets the first round to active
     * @param duelId Id of the duel to start
     * @param userId Id of the User who wants to start the duel
     * @return If starting the duel was successfull
     */
    boolean startDuel(Long duelId, Long userId);

    /**
     * Starts the duel so that nobody can join anymore and sets the first round to active
     * @param duelId Id of the duel to start
     * @param userId Id of the User who wants to start the duel
     * @return Duel that started
     */
    Duel startDuelRest(Long duelId, Long userId);

    /**
     * Saves the selected Answer and determines if it is correct.
     * @param selectedAnswer selected Answer
     * @param duelId current duel
     * @param playerId current player
     */
    boolean saveSelectedAnswer(String selectedAnswer, Long duelId, Long playerId);

    /**
     * Saves the selected Answer and determines if it is correct.
     * @param selectedAnswer selected Answer
     * @param roundId current round
     * @param playerId current player
     */
    boolean saveSelectedAnswerRest(String selectedAnswer, Long roundId, Long playerId);

    /**
     * deactivates current round and activates the next round. If all rounds were played the duel is updated to finished.
     * @param duelId of current duel
     */
    void activateNextRound(Long duelId);

    /**
     * returns not played and not finished duels which the user not joined yet.
     * @return duels
     */
    List<Duel> duelsToJoin(Long loggedInUser);

    /**
     * returns duels that are not started and not finished and the user has joined.
     * @param userId user who wants to start a duel
     * @return duels
     */
    List<Duel> duelsToStart(Long userId);

    /**
     * Returns a list of Strings with the Question, Correct Answer and the wrong answers
     * @param duelStart Id of duel
     * @return content of flashcard
     */
    List<String> playRound(Long duelStart);

    /**
     * Returns a list of Strings with the roundId, Question, Correct Answer and the wrong answers
     * @param roundId Id of current round
     * @return content of flashcard
     */
    List<String> playRoundRest(Long roundId);

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

    /**
     * Generates Wrong answers for a round by using the Levenshtein distance
     * @param correctAnswer The correct Translation
     * @param allTranslationStrings All Translations from the Translation Repo
     * @return 3 wrong answers
     */
    List<String> generateWrongAnswers(String correctAnswer, List<String> allTranslationStrings);

    /**
     * Get not played rounds of a user in a duel.
     * @param duelId id of duel
     * @param userId id of user
     * @return List of not played rounds
     */
    List<Round> getNotPlayedRounds(Long duelId, Long userId);

    /**
     * Get started duels the user has joined.
     * @param userId id of user
     * @return List of playable duels
     */
    List<Duel> duelsToPlay(Long userId);
}
