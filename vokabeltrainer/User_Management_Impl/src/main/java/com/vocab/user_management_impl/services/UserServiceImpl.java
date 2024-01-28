package com.vocab.user_management_impl.services;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management.repos.UserRepo;
import com.vocab.user_management.services.UserService;
import com.vocab.vocabulary_duel_API.repositories.DuelRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@ComponentScan(basePackages = {"com.vocab"})
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepository;

    @Autowired
    private DuelRepo duelRepo;

    public UserEntity findByUsername(String username) throws EntityNotFoundException{
        return userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User mit username " + username + " nicht gefunden."));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity createUser(String userName) {
        try {
            UserEntity existingUser = findByUsername(userName);
            if (existingUser != null) {
                return existingUser;
            }
        } catch (EntityNotFoundException enfe) {
            UserEntity newUser = new UserEntity();
            newUser.setUsername(userName);
            return userRepository.save(newUser);
        }
        return null;
    }


    public UserEntity createUserRest(UserEntity user) {
        try {
            UserEntity existingUser = findByUsername(user.getUsername());

            if (existingUser != null) {
                return existingUser;
            }
        } catch (EntityNotFoundException enfe){
            UserEntity newUser = new UserEntity();
            newUser.setUsername(user.getUsername());
            return userRepository.save(newUser);
        }
        return null;
    }

    @Override
    public boolean deleteUser(Long userId) {
        if (!userRepository.existsById(userId) || duelRepo.existsDuelByPlayersIsContaining(userRepository.findById(userId).get())) {
            return false;
        }
        userRepository.deleteById(userId);
        return true;
    }

    /**
     * /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User nicht gefunden mit id: " + id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }
}
