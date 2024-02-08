package com.vocab.vocabulary_duel_API.services;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management.exceptions.UserNotExistException;
import com.vocab.vocabulary_duel_API.entities.Duel;
import com.vocab.vocabulary_duel_API.entities.Round;
import com.vocab.vocabulary_duel_API.exceptions.*;
import com.vocab.vocabulary_management.exceptions.FlashcardListNotExistException;

import java.util.List;
import java.util.Optional;

public interface DuelService {
    /**
     * Creates a new duel
     * @param userId User id of the user creating the duel
     * @param flashcardListId   Id of the flashcard list to use for the duel
     * @return Duel created
     * @throws UserNotExistException if no user not exists
     * @throws FlashcardListNotExistException if flashcardList not exists
     *
     */
    Duel createDuel(Long userId, Long flashcardListId) throws UserNotExistException, FlashcardListNotExistException;

    /**
     * Lets the user join a duel
     * @param duelId Id of the duel to join
     * @param userId User id of the user joining the duel
     * @return True if the user joined the duel, false otherwise
     * @throws UserNotExistException if user not exists
     * @throws DuelNotExistException if duel not exists
     * @throws DuelAlreadyStartedException if duel already started
     * @throws UserAlreadyPartOfDuelException if user already joined duel
     */
    Boolean joinDuel(long duelId, long userId) throws UserNotExistException, DuelNotExistException, DuelAlreadyStartedException, UserAlreadyPartOfDuelException;

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
     * @throws DuelNotExistException if duel not exists
     */
    List<UserEntity> calculateWinner(Long duelId) throws DuelNotExistException;

    /**
     * Starts the duel so that nobody can join anymore and sets the first round to active
     * @param duelId Id of the duel to start
     * @param userId Id of the User who wants to start the duel
     * @return If starting the duel was successfull
     * @throws UserNotExistException if user not exists
     * @throws DuelNotExistException if duel not exists
     * @throws DuelAlreadyStartedException if duel already started
     * @throws UserNotPartOfDuelException if user not joined duel
     */
    boolean startDuel(Long duelId, Long userId) throws DuelNotExistException, UserNotExistException, UserNotPartOfDuelException, DuelAlreadyStartedException;

    /**
     * Starts the duel so that nobody can join anymore
     * @param duelId Id of the duel to start
     * @param userId Id of the User who wants to start the duel
     * @return Duel that started
     * @throws UserNotExistException if user not exists
     * @throws DuelNotExistException if duel not exists
     * @throws DuelAlreadyStartedException if duel already started
     * @throws UserNotPartOfDuelException if user not joined duel
     */
    Duel startDuelRest(Long duelId, Long userId) throws DuelNotExistException, UserNotPartOfDuelException, DuelAlreadyStartedException, UserNotExistException;

    /**
     * Saves the selected Answer and determines if it is correct.
     * @param selectedAnswer selected Answer
     * @param duelId current duel
     * @param playerId current player
     * @return if saving succeeded
     * @throws UserNotExistException if user not exists
     * @throws DuelNotExistException if duel not exists
     * @throws UserAlreadyPlayedRoundException if user already played the round
     */
    boolean saveSelectedAnswer(String selectedAnswer, Long duelId, Long playerId) throws UserNotExistException, DuelNotExistException, UserAlreadyPlayedRoundException;

    /**
     * Saves the selected Answer and determines if it is correct.
     * @param selectedAnswer selected Answer
     * @param roundId current round
     * @param playerId current player
     * @return if saving succeeded
     * @throws UserNotExistException if user not exists
     * @throws RoundNotExistException if round not exists
     * @throws UserAlreadyPlayedRoundException if user already played the round
     */
    boolean saveSelectedAnswerRest(String selectedAnswer, Long roundId, Long playerId) throws UserNotExistException, RoundNotExistException, UserAlreadyPlayedRoundException;

    /**
     * deactivates current round and activates the next round. If all rounds were played the duel is updated to finished.
     * @param duelId of current duel
     * @throws DuelNotExistException if duel not exists
     */
    void activateNextRound(Long duelId) throws DuelNotExistException;

    /**
     * returns not played and not finished duels which the user not joined yet.
     * @return duels which the user can join
     * @param userId user who wants to join a duel
     * @throws UserNotExistException if user not exists
     */
    List<Duel> duelsToJoin(Long userId) throws UserNotExistException;

    /**
     * returns duels that are not started and not finished and the user has joined.
     * @param userId user who wants to start a duel
     * @return duels which the user can start
     * @throws UserNotExistException if user not exists
     */
    List<Duel> duelsToStart(Long userId) throws UserNotExistException;

    /**
     * Returns a list of Strings with the Question, Correct Answer and the wrong answers
     * @param duelId Id of duel
     * @return content of flashcard
     * @throws DuelNotExistException if duel not exists
     */
    List<String> playRound(Long duelId) throws DuelNotExistException;

    /**
     * Returns a list of Strings with the roundId, Question, Correct Answer and the wrong answers
     * @param roundId Id of current round
     * @return content of flashcard
     * @throws RoundNotExistException if the round not exists
     */
    List<String> playRoundRest(Long roundId) throws RoundNotExistException;

    /**
     * Generate 10 rounds of a newly created duel.
     * @param duelId Id of duel
     * @throws DuelNotExistException if duel not exists
     */
    void generateRounds(Long duelId) throws DuelNotExistException;

    /**
     * Deletes a duel.
     *
     * @param duelId Id of duel
     * @return if deletion succeeded
     * @throws DuelNotExistException if duel not exists
     */
    boolean deleteDuel(Long duelId) throws DuelNotExistException;

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
     * @throws DuelNotExistException if duel not exists
     * @throws UserNotExistException if user not exists
     */
    List<Round> getNotPlayedRounds(Long duelId, Long userId) throws UserNotExistException, DuelNotExistException;

    /**
     * Get started duels the user has joined.
     * @param userId id of user
     * @return List of playable duels
     * @throws UserNotExistException if user not exists
     */
    List<Duel> duelsToPlay(Long userId) throws UserNotExistException;
}
