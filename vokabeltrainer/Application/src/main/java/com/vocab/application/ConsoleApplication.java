package com.vocab.application;

import com.vocab.user_management_impl.services.UserServiceImpl;
import com.vocab.vocabulary_duel.entities.Duel;
import com.vocab.vocabulary_duel_impl.services.DuelServiceImpl;
import com.vocab.vocabulary_management_impl.services.FlashcardListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleApplication implements CommandLineRunner {
    @Autowired
    private final UserServiceImpl userService;
    @Autowired
    private final DuelServiceImpl duelService;
    @Autowired
    private final FlashcardListServiceImpl flashcardListService;

    public ConsoleApplication(UserServiceImpl userService, DuelServiceImpl duelService, FlashcardListServiceImpl flashcardListService) {
        this.userService = userService;
        this.duelService = duelService;
        this.flashcardListService = flashcardListService;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Ask for the username
        System.out.println("Enter your username:");
        String username = scanner.nextLine();

        // Step 2: Create or retrieve the user from the database
        Long userId = userService.createUser(username).getUserId();
        System.out.println("User created/retrieved with ID: " + userId);
        System.out.println(userId);

        // Step 3: Display available Flashcard Lists (Assuming you have a method to retrieve them)
        // Modify this part according to your actual implementation
        System.out.println("Available Flashcard Lists:");
        // Assume getFlashcardLists() is a method in FlashcardListServiceImpl that retrieves all lists
        flashcardListService.getAll().forEach(flashcardList ->
                System.out.println(flashcardList.getFlashcardListId()+" - "+flashcardList.getCategory() + " -- " + flashcardList.getOriginalLanguage() + " - "+flashcardList.getTranslationLanguage()));

        // Step 4: Ask the user to select a Flashcard List
        System.out.println("Enter the ID of the Flashcard List for the duel:");
        Long flashcardListId = scanner.nextLong();

        // Step 5: Create the duel and save it in the database
        Duel duel = duelService.createDuel(userId, flashcardListId);
        System.out.println("Duel created with ID: " + duel.getDuelId());

        // You can add more logic or steps as needed for your application

        scanner.close();
    }
}

