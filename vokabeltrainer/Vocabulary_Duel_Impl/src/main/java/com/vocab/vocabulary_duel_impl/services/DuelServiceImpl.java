package com.vocab.vocabulary_duel_impl.services;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management_impl.services.UserServiceImpl;
import com.vocab.vocabulary_duel.entities.Duel;
import com.vocab.vocabulary_duel.repositories.DuelRepo;
import com.vocab.vocabulary_duel.services.DuelService;
import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management.services.FlashcardListService;
import com.vocab.vocabulary_management_impl.services.FlashcardListServiceImpl;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@ComponentScan(basePackages = {"com.vocab"})
public class DuelServiceImpl implements DuelService {

    @Autowired
    private FlashcardListServiceImpl flashcardListService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private DuelRepo duelRepo;
    /**
     * {@inheritDoc}
     */
    @Override
    public Duel createDuel(long userId, long flashcardListId) {
        Duel duel = new Duel();
        duel.setFlashcardsForDuel(flashcardListService.getById(flashcardListId));
        duel.setPlayer(userService.getById(userId));
        duelRepo.save(duel);
        return duel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean joinDuel(Long duelId, Long userId) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Duel getById(Long id) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Duel> getAll() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserEntity> calculateWinner(Duel duel) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Flashcard> generateFlashcardList(FlashcardList flashcardList, Duel duel) {
        return null;
    }

}
