package com.management.user_management.services;

import com.management.user_management.entities.User;
import java.util.List;

public interface UserService {
    /**
     * Creates a User for playing the vocab game and saves it in the database
     * @param user The User which is to be created
     * @return The created user
     */
    User createUser(User user);

    /**
     * Gets a user from the database by id
     * @param id Id of the user in question
     * @return The user retrieved by the database
     */
    User getById(Long id);

    /**
     * All users which are present in the database
     * @return A list of all users
     */
    List<User> getAll();

}
