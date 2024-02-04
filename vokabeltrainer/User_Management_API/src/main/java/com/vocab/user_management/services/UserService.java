package com.vocab.user_management.services;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management.exceptions.UserNotExistException;
import com.vocab.user_management.exceptions.UserStillPlaysException;

import java.util.List;

public interface UserService {
    /**
     * Creates a User for playing the vocab game and saves it in the database
     * @param userName The User which is to be created
     * @return The created user or null
     */

    UserEntity createUser(String userName);

    UserEntity createUserRest(UserEntity user);
    /**
     * Deletes a user from the database if the user is not involved in a duel.
     *
     * @param id Id of the user in question
     * @return true if deletion succeeded; false if user participate in a duel
     * @throws UserStillPlaysException when the user participates in a duel
     * @throws UserNotExistException when the user does not exist
     */
    boolean deleteUser(Long id) throws UserStillPlaysException, UserNotExistException;

    /**
     * Gets a user from the database by id
     * @param id Id of the user in question
     * @return The user retrieved by the database
     * @throws UserNotExistException when the user does not exist
     */
    UserEntity getById(Long id) throws UserNotExistException;

    /**
     * All users which are present in the database
     * @return A list of all users
     */
    List<UserEntity> getAll();

    /**
     * Finds a user by username
     * @param username String
     * @return user found user
     * @throws UserNotExistException when the user does not exist
     */
    UserEntity findByUsername(String username) throws UserNotExistException;

}
