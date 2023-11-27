package com.vocab.vocabulary_duel_impl.services;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management_impl.services.UserServiceImpl;
import com.vocab.vocabulary_duel.entities.Duel;
import com.vocab.vocabulary_duel.entities.Round;
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
        UserEntity user = userService.getById(userId);
        boolean isAlreadyJoined = duelRepo.findById(duelId).get().getPlayers().contains(user);
        if(!duel.isStarted()&&!isAlreadyJoined) {
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


    public void generateRounds(Long duelId) {
        Duel duel = duelRepo.findById(duelId).get();
        List<Flashcard> flashcards = duel.getFlashcardsForDuel().getFlashcards();
        for(int i=0; i<10;i++){
            Round round =
        }

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
