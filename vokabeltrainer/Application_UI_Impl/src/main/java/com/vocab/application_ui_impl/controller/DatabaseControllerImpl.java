package com.vocab.application_ui_impl.controller;

import com.vocab.application_ui_impl.views.DbView;
import com.vocab.user_management.repos.UserRepo;
import com.vocab.vocabulary_duel.repositories.DuelRepo;
import com.vocab.vocabulary_management.repos.FlashcardListRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;

@Controller
public class DatabaseControllerImpl {
    private final UserRepo userRepository;
    private final DuelRepo duelRepository;
    private final FlashcardListRepo flashcardListRepository;

    private final DbView dbView;


    public DatabaseControllerImpl(UserRepo userRepository, DuelRepo duelRepository, FlashcardListRepo flashcardListRepository, DbView dbView) {
        this.userRepository = userRepository;
        this.duelRepository = duelRepository;
        this.flashcardListRepository = flashcardListRepository;
        this.dbView = dbView;
        // Initialisiere weiterer Repositories
    }

    @Transactional
    public void clearDatabase() {

        dbView.displayOptions();
        String confirmation = dbView.readString();
        if (confirmation.equalsIgnoreCase("y")) {
            // Lösche alle Entitäten
            duelRepository.deleteAll();
            userRepository.deleteAll();
            flashcardListRepository.deleteAll();
            // Füge hier Befehle zum Löschen aus weiteren Repositories hinzu

            dbView.printSuccessMessage();
        } else {
            dbView.printFailMessage();
        }
    }


}
