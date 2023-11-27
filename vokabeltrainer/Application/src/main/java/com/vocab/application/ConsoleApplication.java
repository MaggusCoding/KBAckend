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
        boolean exit = false;
        while (!exit) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Create/Retrieve Another Player");
            System.out.println("2. Create Duel");
            System.out.println("3. Join Existing Duel");
            System.out.println("4. Start Duel");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    // Step 1: Ask for the username
                    System.out.println("Enter your username:");
                    scanner.nextLine();
                    String username = scanner.nextLine();

                    // Step 2: Create or retrieve the user from the database
                    Long userId = userService.createUser(username).getUserId();
                    System.out.println("User created/retrieved with ID: " + userId);
                    break;
                case 2:
                    System.out.println("Select User:");
                    userService.getAll().forEach(userEntity ->
                            System.out.println(userEntity.getUserId()+" - "+userEntity.getUsername()));
                    System.out.println("Enter ID of the user:");
                    Long userIdDuel = scanner.nextLong();
                    // Step 3: Display available Flashcard Lists (Assuming you have a method to retrieve them)
                    // Modify this part according to your actual implementation
                    System.out.println("Available Flashcard Lists:");
                    // Assume getFlashcardLists() is a method in FlashcardListServiceImpl that retrieves all lists
                    flashcardListService.getAll().forEach(flashcardList ->
                            System.out.println(flashcardList.getFlashcardListId() + " - " + flashcardList.getCategory() + " -- " + flashcardList.getOriginalLanguage() + " - " + flashcardList.getTranslationLanguage()));

                    // Step 4: Ask the user to select a Flashcard List
                    System.out.println("Enter the ID of the Flashcard List for the duel:");
                    Long flashcardListId = scanner.nextLong();

                    // Step 5: Create the duel and save it in the database
                    Duel duel = duelService.createDuel(userIdDuel, flashcardListId);
                    System.out.println("Duel created with ID: " + duel.getDuelId());
                    break;
                case 3:
                    System.out.println("Join Duel:");
                    break;
                case 4:
                    System.out.println("Start Duel:");
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }
}

