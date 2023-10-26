package com.management.user_management.services;

import com.management.user_management.UserAlreadyExistsException;
import com.management.user_management.entities.User;

import java.util.List;

public class UserServiceImpl implements UserService{

    /**
     * {@inheritDoc}
     */
    @Override
    public User createUser(User user) throws UserAlreadyExistsException {
        return null;
    }

    /**
     /**
     * {@inheritDoc}
     */
    @Override
    public User getById(Long id) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getAll() {
        return null;
    }
}
