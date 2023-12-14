package com.vocab.application;

import com.vocab.application.serviceImpl.ImportServiceImpl;
import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management_impl.services.UserServiceImpl;
import com.vocab.vocabulary_duel.entities.Answer;
import com.vocab.vocabulary_duel.entities.Duel;
import com.vocab.vocabulary_duel.entities.Round;
import com.vocab.vocabulary_duel_impl.services.DuelServiceImpl;
import com.vocab.vocabulary_management_impl.services.FlashcardListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @todo
 * 1. Try Catch Blöcke für alle Auswahlmöglichkeiten zwecks Exception Handling oder if Blöcke
 *  -> Bei Mehrspieler Duell Auswahl von Spielern implementieren und abfangen wenn Spieler selection outta bounds
 *  -> Standard User muss in der Datenbank vorhanden sein oder abfangen
 * 2. Javadoc überprüfen / Aufräumen Sachen die wir nicht brauchen
 * 3. Testfälle für alle Service Klassen neu machen mit Mockito siehe: https://github.com/MaggusCoding/webtechKassensystemBackend
 * (4.) Evtl. Model View Controller für Frontend evtl.
 * 5. Fertiggespielte Duels nicht mehr anzeigen bei Join und Start Duel wegen Exception und so auch gucken
 * 6. Duel Lösch Funktionalität
 * 7. Einfach mal weng rumzocken in der Anwendung
 */
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

    private Long loggendInUser = 1L;

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
        while (!exit) {
            print("\nMain Menu:");
            print("1. Create/Retrieve Another Player");
            print("2. Create Duel");
            print("3. Join Existing Duel");
            print("4. Start Duel");
            print("5. Import/Delete FlashcardList");
            print("6. Exit");
            print("Enter your choice: ");
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
                    print("Select Duel to Start:");
                    duelService.getAll().forEach(duel1 ->
                            print(duel1.getDuelId() + " - " + duel1.getFlashcardsForDuel().getCategory()));
                    print("Enter the ID of the Duel to start:");
                    Long duelStart = scanner.nextLong();
                    duelService.startDuel(duelStart);
                    List<String> flashcardString = duelService.playRound(duelStart);
                    boolean duelFinished = false;
                    for (int x = 1; x <= 10; x++) {
                        // Player abort the round
                        if(!playOneRound(flashcardString, scanner, duelStart)){
                            break;
                        }
                        flashcardString = duelService.playRound(duelStart);
                        print("Let´s begin the next round!");
                        if(x == 10){
                            duelFinished = true;
                        }
                    }
                    if(duelFinished){
                        print("All rounds were played. Let´s see who wins! ");
                        print("The winner is/are " + duelService.calculateWinner(duelStart).stream().map(UserEntity::getUsername).collect(Collectors.joining(",")));
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
                            if (successDelete) {
                                print("FlashcardList konnte gelöscht werden.");
                            } else {
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

    private boolean playOneRound(List<String> flashcardString, Scanner scanner, Long duelStart) {
        boolean allUsersPlayed = false;
        while (!allUsersPlayed) {
            print("##############You are now " + userService.getById(loggendInUser).getUsername() + "#######################");
            print("What is the translation for: " + flashcardString.get(0) + "? (Enter the id of the answer.)");

            for (int i = 1; i < 5; i++) {
                print(i + "." + flashcardString.get(i));
            }
            int selectedAnswer = scanner.nextInt();
            print("Your answer: " + flashcardString.get(selectedAnswer));
            duelService.saveSelectedAnswer(flashcardString.get(selectedAnswer), duelStart, loggendInUser);

            Long nextUser = printChooseNextPlayer(duelStart, scanner);
            switch (nextUser.intValue()) {
                case -1:
                    print("All players played this round.");
                    allUsersPlayed = true;
                    break;
                case -2:
                    print("Typed username is incorrect: " + nextUser + ". Play this flashcard again!");
                    break;
                case -3:
                    // Typed exit
                    return false;
                default:
                    loggendInUser = nextUser;

            }
        }
        duelService.activateNextRound(duelStart);
        return true;
    }

    private Long printChooseNextPlayer(Long duelStart, Scanner scanner) {
        List<String> usernames = getUsersWhoNotPlayed(duelStart);
        if (usernames.isEmpty()) {
            return -1L;
        } else {
            print("Which user should answer next? (Enter username or type exit to leave)");
            print(usernames.stream().collect(Collectors.joining(System.lineSeparator())));
            String nextUser = scanner.next();
            UserEntity user = userService.findByUsername(nextUser).orElse(null);
            if (user == null) {
                return -2L;
            } else if (nextUser.equalsIgnoreCase("exit")) {
                return -3L;
            }
            return user.getUserId();
        }
    }

    private List<String> getUsersWhoNotPlayed(Long duelStart) {
        List<UserEntity> usersAlreadyPlayed = duelService.getById(duelStart).get()
                .getRounds().stream().filter(Round::isActiveRound).findFirst().get()
                .getSelectedAnswers().stream().map(Answer::getPlayer).toList();
        List<UserEntity> players = duelService.getById(duelStart).get().getPlayers();
        if (usersAlreadyPlayed.size() == players.size()) {
            return List.of();
        }
        return players.stream().filter(player -> !usersAlreadyPlayed.contains(player)).map(UserEntity::getUsername).collect(Collectors.toList());
    }

    void print(String text) {
        System.out.println(text);
    }
}

