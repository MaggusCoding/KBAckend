package com.vocab.user_management_impl.services;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management.services.UserService;
import com.vocab.user_management_impl.repos.UserRepo;
import com.vocab.vocabulary_duel_impl.repos.DuelRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@ComponentScan(basePackages = {"com.vocab"})
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepository;

    @Autowired
    private DuelRepo duelRepo;

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


    public UserEntity createUserRest(UserEntity user) {
        Optional<UserEntity> existingUser = findByUsername(user.getUsername());

        return existingUser.orElseGet(() -> {
            UserEntity newUser = new UserEntity();
            newUser.setUsername(user.getUsername());
            return userRepository.save(newUser);
        });
    }

    @Override
    public boolean deleteUser(Long userId) {
        // TODO: Prüfung ob user in einem duel steckt über Service lösen maybe
        if (!userRepository.existsById(userId) || duelRepo.existsDuelByPlayersIsContaining(userRepository.findById(userId).get())) {
            return false;
        }
        userRepository.deleteById(userId);
        return true;
    }

    /**
     /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }
}
