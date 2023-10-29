package com.vocab.vocabulary_duel.services;

import com.management.user_management.entities.User;
import com.vocab.vocabulary_duel.entities.Duel;
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
     * Calculates a winner for a duel by comparing the scores of all the users
     * @param duel Duel to calculate the winner for
     * @return User that won the duel
     */
    User calculateWinner(Duel duel);

}
