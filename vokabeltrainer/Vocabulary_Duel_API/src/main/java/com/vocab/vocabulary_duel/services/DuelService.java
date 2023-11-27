package com.vocab.vocabulary_duel.services;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.vocabulary_duel.entities.Duel;
import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.FlashcardList;

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
     * Calculates the winners for a duel by comparing the scores of all the users
     * @param duel Duel to calculate the winner for
     * @return List of Users that won the duel
     */
    List<UserEntity> calculateWinner(Duel duel);



    boolean startDuel(Long duelId);
}
