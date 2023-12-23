package com.vocab.application.controller;

import com.vocab.user_management.repos.UserRepo;
import com.vocab.vocabulary_duel.repositories.DuelRepo;
import com.vocab.vocabulary_management.repos.FlashcardListRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;

import java.util.Scanner;

@Controller
public class DatabaseController {
    private final UserRepo userRepository;
    private final DuelRepo duelRepository;
    private final FlashcardListRepo flashcardListRepository;


    public DatabaseController(UserRepo userRepository, DuelRepo duelRepository, FlashcardListRepo flashcardListRepository /*, weitere Repositories */) {
        this.userRepository = userRepository;
        this.duelRepository = duelRepository;
        this.flashcardListRepository = flashcardListRepository;
        // Initialisiere weitere Repositories
    }

    @Transactional
    public void clearDatabase(Scanner scanner) {
        System.out.println("This will clear the in-memory database completely! Are you sure? (y/n)");
        String confirmation = scanner.next();
        if (confirmation.equalsIgnoreCase("y")) {
            // Lösche alle Entitäten
            userRepository.deleteAll();
            duelRepository.deleteAll();
            flashcardListRepository.deleteAll();
            // Füge hier Befehle zum Löschen aus weiteren Repositories hinzu

            System.out.println("In-memory database cleared successfully.");
        } else {
            System.out.println("Database clear cancelled.");
        }
    }
}
