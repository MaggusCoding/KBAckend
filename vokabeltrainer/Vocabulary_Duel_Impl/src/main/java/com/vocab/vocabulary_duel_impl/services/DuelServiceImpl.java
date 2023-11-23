package com.vocab.vocabulary_duel_impl.services;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management_impl.services.UserServiceImpl;
import com.vocab.vocabulary_duel.entities.Duel;
import com.vocab.vocabulary_duel.repositories.DuelRepo;
import com.vocab.vocabulary_duel.services.DuelService;
import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management_impl.services.FlashcardListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Duel createDuel(Long userId, Long flashcardListId) {
        Duel duel = new Duel();
        duel.setFlashcardsForDuel(flashcardListService.getById(flashcardListId));
        duel.setPlayer(userService.getById(userId));
        duel.setStarted(false);
        duelRepo.save(duel);
        return duel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean joinDuel(long duelId, long userId) {
        Duel duel = duelRepo.findById(duelId).orElseThrow();
        if(!duel.isStarted()) {
            duel.setPlayer(userService.getById(userId));
            duelRepo.save(duel);
            return true;
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Duel> getById(Long id) {
        return duelRepo.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Duel> getAll() {
        return duelRepo.findAll();
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

    public boolean startDuel(Long duelId){
        Duel duel = null;
       if(duelRepo.findById(duelId).isPresent()) {
         duel = duelRepo.findById(duelId).get();
       }else return false;
       duel.setStarted(true);

       return true;
    }
}
