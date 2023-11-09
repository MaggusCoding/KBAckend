package com.management.user_management.services;

import com.management.user_management.UserAlreadyExistsException;
import com.management.user_management.entities.UserEntity;

import java.util.List;

public interface UserService {
    /**
     * Creates a User for playing the vocab game and saves it in the database
     * @param userEntity The User which is to be created
     * @return The created user
     */
    UserEntity createUser(UserEntity userEntity) throws UserAlreadyExistsException;

    /**
     * Gets a user from the database by id
     * @param id Id of the user in question
     * @return The user retrieved by the database
     */
    UserEntity getById(Long id);

    /**
     * All users which are present in the database
     * @return A list of all users
     */
    List<UserEntity> getAll();

}
