package com.vocab.user_management_impl.services;

import com.management.user_management.entities.UserEntity;
import com.management.user_management.repos.UserRepo;
import com.management.user_management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@ComponentScan(basePackages = {"com.management"})
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepository;


    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity createUser(String userName) {
        Optional<UserEntity> existingUser = findByUsername(userName);

        return existingUser.orElseGet(() -> {
            UserEntity newUser = new UserEntity();
            newUser.setUsername(userName);
            return userRepository.save(newUser);
        });
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }
}
