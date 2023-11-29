package com.vocab.application;

import com.vocab.application.serviceImpl.ImportServiceImpl;
import com.vocab.user_management_impl.services.UserServiceImpl;
import com.vocab.vocabulary_duel.entities.Duel;
import com.vocab.vocabulary_duel_impl.services.DuelServiceImpl;
import com.vocab.vocabulary_management_impl.services.FlashcardListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

@Component
public class ConsoleApplication implements CommandLineRunner {
    @Autowired
    private final UserServiceImpl userService;
    @Autowired
    private final DuelServiceImpl duelService;
    @Autowired
    private final FlashcardListServiceImpl flashcardListService;
    @Autowired
    private final ImportServiceImpl importService;

    public ConsoleApplication(UserServiceImpl userService, DuelServiceImpl duelService, FlashcardListServiceImpl flashcardListService, ImportServiceImpl importService) {
        this.userService = userService;
        this.duelService = duelService;
        this.flashcardListService = flashcardListService;
        this.importService = importService;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        Long loggendInUser = 1L;
        while (!exit) {
            print("\nMain Menu:");
            print("1. Create/Retrieve Another Player");
            print("2. Create Duel");
            print("3. Join Existing Duel");
            print("4. Start Duel");
            print("5. Import/Delete FlashcardList");
            print("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    // Step 1: Ask for the username
                    print("Enter your username:");
                    scanner.nextLine();
                    String username = scanner.nextLine();

                    // Step 2: Create or retrieve the user from the database
                    Long userId = userService.createUser(username).getUserId();
                    print("User created/retrieved with ID: " + userId);
                    loggendInUser = userId;
                    break;
                case 2:
                    print("Select User:");
                    userService.getAll().forEach(userEntity ->
                            print(userEntity.getUserId() + " - " + userEntity.getUsername()));
                    print("Enter ID of the user:");
                    Long userIdDuel = scanner.nextLong();
                    // Step 3: Display available Flashcard Lists (Assuming you have a method to retrieve them)
                    // Modify this part according to your actual implementation
                    print("Available Flashcard Lists:");
                    // Assume getFlashcardLists() is a method in FlashcardListServiceImpl that retrieves all lists
                    flashcardListService.getAll().forEach(flashcardList ->
                            print(flashcardList.getFlashcardListId() + " - " + flashcardList.getCategory() + " -- " + flashcardList.getOriginalLanguage() + " - " + flashcardList.getTranslationLanguage()));

                    // Step 4: Ask the user to select a Flashcard List
                    print("Enter the ID of the Flashcard List for the duel:");
                    Long flashcardListId = scanner.nextLong();

                    // Step 5: Create the duel and save it in the database
                    Duel duel = duelService.createDuel(userIdDuel, flashcardListId);
                    duelService.generateRounds(duel.getDuelId());
                    print("Duel created with ID: " + duel.getDuelId());
                    break;
                case 3:
                    print("Select Duel to Join:");
                    duelService.getAll().forEach(duel1 ->
                            print(duel1.getDuelId() + " - " + duel1.getFlashcardsForDuel().getCategory()));
                    print("Enter the ID of the Duel to Join:");
                    Long duelJoin = scanner.nextLong();
                    boolean success = duelService.joinDuel(duelJoin, loggendInUser);
                    if (success) {
                        print("Successfully joined Duel");
                    } else {
                        print("Logged in User already Joined Duel");
                    }
                    break;
                case 4:
                    System.out.println("Select Duel to Start:");
                    duelService.getAll().forEach(duel1 ->
                            System.out.println(duel1.getDuelId()+" - "+duel1.getFlashcardsForDuel().getCategory()));
                    System.out.println("Enter the ID of the Duel to start:");
                    Long duelStart = scanner.nextLong();
                    duelService.startDuel(duelStart);
                    System.out.println("What is the translation for: "+duelService.playRound(duelStart).get(0)+"?") ;
                    for(int i=1;i<5;i++){
                        System.out.println(i+"."+duelService.playRound(duelStart).get(i));
                    }
                    break;
                case 5:
                    print("1. Import new FlashcardList");
                    print("2. Import initial FlashcardLists");
                    print("3. Delete a FlashcardList");
                    print("Enter your choice: ");
                    int option = scanner.nextInt();
                    switch (option) {
                        case 1:
                            print("Enter an absolute filepath: ");
                            String path = scanner.next();
                            try {
                                if (importService.importFile(path)) {
                                    print("Importing new Flashcardlist was successful.");
                                }
                            } catch (FileNotFoundException fex) {
                                print("Zu dem angegebenen Pfad existiert keine Datei. Pfad: " + path);
                            } catch (Exception ex) {
                                print("Etwas ist schief gelaufen. Kontaktieren Sie ihren Administrator.");
                            }
                            break;
                        case 2:
                            try {
                                boolean importSuccess = importService.importInitialFiles();
                                if (importSuccess) {
                                    print("initiale Flashcards wurden importiert.");
                                }
                            } catch (IOException ioex) {
                                if (ioex instanceof FileNotFoundException) {
                                    print("Zu dem angegebenen Pfad existiert keine Datei. Fehlernachricht: " + ioex.getMessage());
                                } else {
                                    print("Etwas ist schief gelaufen. Kontaktieren Sie ihren Administrator.");
                                }
                            }
                            break;
                        case 3:
                            print("Existing FlashcardList: ");
                            flashcardListService.getAll().forEach(flashcardList ->
                                    print(flashcardList.getFlashcardListId() + " - " + flashcardList.getCategory() + " -- " + flashcardList.getOriginalLanguage() + " - " + flashcardList.getTranslationLanguage()));
                            print("Enter the id of the FlashcardList to delete: ");
                            Long id = scanner.nextLong();
                            boolean successDelete = flashcardListService.deleteFlashcardList(id);
                            if(successDelete){
                                print("FlashcardList konnte gelöscht werden.");
                            }else{
                                print("FlashcardList konnte nicht gelöscht werden. Evtl. existiert noch ein Duell, die die FlashcardList nutzt.");
                            }
                            break;
                        default:
                            print("Invalid choice. Please enter a number between 1 and 3.");
                    }
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    print("Invalid choice. Please enter a number between 1 and 6.");
            }
        }
        System.exit(0);
    }

    void print(String text) {
        System.out.println(text);
    }
}

