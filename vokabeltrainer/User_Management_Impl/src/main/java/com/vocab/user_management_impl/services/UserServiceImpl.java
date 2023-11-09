package com.vocab.user_management_impl.services;

import com.management.user_management.UserAlreadyExistsException;
import com.management.user_management.entities.UserEntity;
import com.management.user_management.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity createUser(UserEntity userEntity) throws UserAlreadyExistsException {
        return null;
    }

    /**
     /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity getById(Long id) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserEntity> getAll() {
        return null;
    }
}
