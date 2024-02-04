package com.vocab.user_management_impl.services;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management.exceptions.UserNotExistException;
import com.vocab.user_management.exceptions.UserStillPlaysException;
import com.vocab.user_management.repos.UserRepo;
import com.vocab.user_management.services.UserService;
import com.vocab.vocabulary_duel_API.repositories.DuelRepo;
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

    /**
     * {@inheritDoc}
     */
    public UserEntity findByUsername(String username) throws UserNotExistException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotExistException("User mit username " + username + " nicht gefunden."));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity createUser(String userName) {
        try {
            UserEntity existingUser = findByUsername(userName);
            return existingUser;
        } catch (UserNotExistException e) {
            UserEntity newUser = new UserEntity();
            newUser.setUsername(userName);
            return userRepository.save(newUser);
        }
    }

    /**
     * {@inheritDoc}
     */
    public UserEntity createUserRest(UserEntity user) {
        try {
            UserEntity existingUser = findByUsername(user.getUsername());
            return existingUser;
        } catch (UserNotExistException e){
            UserEntity newUser = new UserEntity();
            newUser.setUsername(user.getUsername());
            return userRepository.save(newUser);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteUser(Long userId) throws UserStillPlaysException, UserNotExistException {
        if (!userRepository.existsById(userId)) {
            throw new UserNotExistException("User nicht gefunden mit id: " + userId);
        }
        if(duelRepo.existsDuelByPlayersIsContaining(userRepository.findById(userId).get())){
            throw new UserStillPlaysException("User mit id: " + userId + " spielt noch in einem Duel. Bitte lösche die Duelle vorher.");
        }
        userRepository.deleteById(userId);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity getById(Long id) throws UserNotExistException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotExistException("User nicht gefunden mit id: " + id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }
}
