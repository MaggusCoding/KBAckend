package com.vocab.user_management_impl.services;

import com.management.user_management.UserAlreadyExistsException;
import com.management.user_management.entities.User;
import com.management.user_management.services.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

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
