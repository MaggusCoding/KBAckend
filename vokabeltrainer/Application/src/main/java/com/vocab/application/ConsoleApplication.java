package com.vocab.application;

import com.vocab.application.serviceImpl.ImportServiceImpl;
import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management_impl.services.UserServiceImpl;
import com.vocab.vocabulary_duel.entities.Answer;
import com.vocab.vocabulary_duel.entities.Duel;
import com.vocab.vocabulary_duel.entities.Round;
import com.vocab.vocabulary_duel_impl.services.DuelServiceImpl;
import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management_impl.services.FlashcardListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * TODO: 1. Try Catch Blöcke für alle Auswahlmöglichkeiten zwecks Exception Handling oder if Blöcke
 * 2. Javadoc überprüfen / Aufräumen Sachen die wir nicht brauchen
 * 3. Testfälle für alle Service Klassen neu machen mit Mockito siehe: https://github.com/MaggusCoding/webtechKassensystemBackend
 * (4.) Evtl. Model View Controller für Frontend evtl.
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

    private boolean optionInvalid = true;

    public ConsoleApplication(UserServiceImpl userService, DuelServiceImpl duelService, FlashcardListServiceImpl flashcardListService, ImportServiceImpl importService) {
        this.userService = userService;
        this.duelService = duelService;
        this.flashcardListService = flashcardListService;
        this.importService = importService;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        // default user
        loggendInUser = userService.createUser("god").getUserId();
        boolean exit = false;
        while (!exit) {
            print("\nMain Menu:");
            print("0. Clear DB(for convenience)");
            print("1. Create/Retrieve Another Player");
            print("2. Create Duel");
            print("3. Join Existing Duel");
            print("4. Start Duel");
            print("5. Delete a Duel");
            print("6. Import/Delete FlashcardList");
            print("7. Exit");
            print("Enter your choice: ");
            int choice = -1;
            try {
                choice = scanner.nextInt();
                switch (choice) {
                    case 0:
                        print("clears the db completely! You sure? y or n");
                        if (scanner.next().equalsIgnoreCase("Y")) {
                            print("not yet implemented");
                        } else {
                            print("Nothing happened.");
                        }
                        break;
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
                        List<UserEntity> users = userService.getAll();
                        users.forEach(userEntity ->
                                print(userEntity.getUserId() + " - " + userEntity.getUsername()));
                        Long userIdDuel = null;
                        optionInvalid = true;
                        while (optionInvalid) {
                            print("Enter ID of the user:");
                            try {
                                userIdDuel = scanner.nextLong();
                                Long finalUserIdDuel = userIdDuel;
                                if (users.stream().anyMatch(user -> user.getUserId().equals(finalUserIdDuel))) {
                                    optionInvalid = false;
                                } else {
                                    print("Entered ID is invalid. Try again!");
                                }
                            } catch (InputMismatchException e) {
                                print("Entered ID is invalid. Try again!");
                                scanner.next();
                            }
                        }
                        // Step 3: Display available Flashcard Lists (Assuming you have a method to retrieve them)
                        // Modify this part according to your actual implementation
                        print("Available Flashcard Lists:");
                        // Assume getFlashcardLists() is a method in FlashcardListServiceImpl that retrieves all lists
                        List<FlashcardList> flashcardLists = flashcardListService.getAll();
                        flashcardLists.forEach(flashcardList ->
                                print(flashcardList.getFlashcardListId() + " - " + flashcardList.getCategory() + " -- " + flashcardList.getOriginalLanguage() + " - " + flashcardList.getTranslationLanguage()));

                        // Step 4: Ask the user to select a Flashcard List
                        Long flashcardListId = null;
                        optionInvalid = true;
                        while (optionInvalid) {
                            try {
                                print("Enter the ID of the Flashcard List for the duel:");
                                flashcardListId = scanner.nextLong();
                                Long finalFlashcardListId = flashcardListId;
                                if (flashcardLists.stream().anyMatch(flashcardList -> flashcardList.getFlashcardListId().equals(finalFlashcardListId))) {
                                    optionInvalid = false;
                                } else {
                                    print("Entered ID is invalid. Try again!");
                                }
                            } catch (InputMismatchException e) {
                                print("Entered ID is invalid. Try again!");
                                scanner.next();
                            }
                        }
                        // Step 5: Create the duel and save it in the database
                        Duel duel = duelService.createDuel(userIdDuel, flashcardListId);
                        duelService.generateRounds(duel.getDuelId());
                        print("Duel created with ID: " + duel.getDuelId());
                        break;
                    case 3:
                        print("Select Duel to Join:");
                        List<Duel> duelsToJoin = duelService.duelsToJoin();
                        duelsToJoin.forEach(duel1 ->
                                print(duel1.getDuelId() + " - " + duel1.getFlashcardsForDuel().getCategory()));
                        optionInvalid = true;
                        Long duelJoin = null;
                        while (optionInvalid) {
                            print("Enter the ID of the Duel to Join:");
                            try {
                                duelJoin = scanner.nextLong();
                                Long finalDuelJoin = duelJoin;
                                if (duelsToJoin.stream().anyMatch(duelTemp -> duelTemp.getDuelId().equals(finalDuelJoin))) {
                                    optionInvalid = false;
                                } else {
                                    print("Entered ID is invalid. Try again!");
                                }
                            } catch (InputMismatchException e) {
                                print("Entered ID is invalid. Try again!");
                                scanner.next();
                            }

                        }
                        boolean success = duelService.joinDuel(duelJoin, loggendInUser);
                        if (success) {
                            print("Successfully joined Duel");
                        } else {
                            print("Logged in User already Joined Duel");
                        }
                        break;
                    case 4:
                        List<Duel> duelsToStart = duelService.duelsToStart(loggendInUser);
                        if (!duelsToStart.isEmpty()) {
                            print("Select Duel to Start:");
                            duelsToStart.forEach(duel1 ->
                                    print(duel1.getDuelId() + " - " + duel1.getFlashcardsForDuel().getCategory()));
                            optionInvalid = true;
                            Long duelStart = null;
                            while (optionInvalid) {
                                print("Enter the ID of the Duel to start:");
                                try {
                                    duelStart = scanner.nextLong();
                                    Long finalDuelStart = duelStart;
                                    if (duelsToStart.stream().anyMatch(duelTemp2 -> duelTemp2.getDuelId().equals(finalDuelStart))) {
                                        optionInvalid = false;
                                    } else {
                                        print("Entered ID is invalid. Try again!");
                                    }
                                } catch (InputMismatchException e) {
                                    print("Entered ID is invalid. Try again!");
                                    scanner.next();
                                }
                            }
                            duelService.startDuel(duelStart);
                            List<String> flashcardString = duelService.playRound(duelStart);
                            boolean duelFinished = false;
                            for (int x = 1; x <= 10; x++) {
                                print("#################### round " + x + "/10 ####################");
                                // Player abort the round
                                if (!playOneRound(flashcardString, scanner, duelStart)) {
                                    break;
                                }
                                if (x == 10) {
                                    duelFinished = true;
                                } else {
                                    flashcardString = duelService.playRound(duelStart);
                                    print("Let´s begin the next round!");
                                }
                            }
                            if (duelFinished) {
                                print("All rounds were played. Let´s see who wins! ");
                                String winners = duelService.calculateWinner(duelStart).stream().map(UserEntity::getUsername).collect(Collectors.joining(","));
                                if (winners.isEmpty()) {
                                    print("Everybody answered wrong! Nobody wins!");
                                } else {
                                    print("The winner is/are " + winners);
                                }
                            }
                        } else {
                            print("Apparently you didn´t join a duel.");
                        }
                        break;
                    case 5:
                        print("Delete one of the following duels: ");
                        List<Duel> duels = duelService.getAll();
                        duels.forEach(duelTemp -> print(duelTemp.getDuelId() + " - " + duelTemp.getFlashcardsForDuel().getCategory() + " - Players: " + duelTemp.getPlayers().stream().map(UserEntity::getUsername).collect(Collectors.joining(","))));
                        optionInvalid = true;
                        while (optionInvalid) {
                            print("Enter the ID of the duel you want to delete: ");
                            try {
                                Long duelToDelete = scanner.nextLong();
                                if (duelService.deleteDuel(duelToDelete)) {
                                    print("Deletion was successful.");
                                    optionInvalid = false;
                                } else {
                                    print("Couldn't delete the duel because it doesn´t exist.");
                                }
                            } catch (InputMismatchException e) {
                                print("Entered ID is invalid. Try again!");
                                scanner.next();
                            }
                        }
                        break;
                    case 6:
                        print("1. Import new FlashcardList");
                        print("2. Import initial FlashcardLists");
                        print("3. Delete a FlashcardList");
                        print("Enter your choice: ");
                        optionInvalid = true;
                        int option = 0;
                        while (optionInvalid) {
                            try {
                                option = scanner.nextInt();
                                if (0 < option && option < 4) {
                                    optionInvalid = false;
                                } else {
                                    print("Entered ID is invalid. Try again!");
                                }
                            } catch (InputMismatchException e) {
                                print("Entered ID is invalid. Try again!");
                                scanner.next();
                            }
                        }
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
                                print("Existing FlashcardLists: ");
                                flashcardListService.getAll().forEach(flashcardList ->
                                        print(flashcardList.getFlashcardListId() + " - " + flashcardList.getCategory() + " -- " + flashcardList.getOriginalLanguage() + " - " + flashcardList.getTranslationLanguage()));
                                print("Enter the id of the FlashcardList to delete: ");
                                Long id = scanner.nextLong();
                                boolean successDelete = flashcardListService.deleteFlashcardList(id);
                                if (successDelete) {
                                    print("FlashcardList konnte gelöscht werden.");
                                } else {
                                    print("FlashcardList konnte nicht gelöscht werden. Evtl. existiert noch ein Duel, die die FlashcardList nutzt.");
                                }
                                break;
                            default:
                        }
                        break;
                    case 7:
                        exit = true;
                        break;
                    default:
                        print("Invalid choice. Please enter a number between 1 and 7.");
                }
            } catch (InputMismatchException e) {
                print("Entered ID is invalid. Try again!");
                scanner.next();
            }
        }
        System.exit(0);
    }

    private boolean playOneRound(List<String> flashcardString, Scanner scanner, Long duelStart) {
        boolean allUsersPlayed = false;
        while (!allUsersPlayed) {
            print("#################### You are now " + userService.getById(loggendInUser).getUsername() + " ####################");
            print("What is the translation for: " + flashcardString.get(0) + "? (Enter the id of the answer.)");

            for (int i = 1; i < 5; i++) {
                print(i + "." + flashcardString.get(i));
            }
            int selectedAnswer = 0;
            optionInvalid = true;
            while (optionInvalid) {
                try {
                    selectedAnswer = scanner.nextInt();
                    if (0 < selectedAnswer && selectedAnswer < 5) {
                        optionInvalid = false;
                    } else {
                        print("Entered ID is invalid. Try again!");
                    }
                } catch (InputMismatchException e) {
                    print("Entered ID is invalid. Try again!");
                    scanner.next();
                }
            }
            print("Your answer: " + flashcardString.get(selectedAnswer));
            duelService.saveSelectedAnswer(flashcardString.get(selectedAnswer), duelStart, loggendInUser);

            boolean nextPlayerChosen = false;
            while (!nextPlayerChosen) {
                Long nextUser = printChooseNextPlayer(duelStart, scanner);
                switch (nextUser.intValue()) {
                    case -1:
                        print("All players played this round.");
                        allUsersPlayed = true;
                        nextPlayerChosen = true;
                        break;
                    case -2:
                        print("Typed username does not exist. Choose other player!");
                        break;
                    case -3:
                        // Typed exit
                        return false;
                    case -4:
                        print("This user already played this round! Choose other player!");
                        break;
                    case -5:
                        print("This user is not participating in the current duel! Choose other player!");
                        break;
                    default:
                        loggendInUser = nextUser;
                        nextPlayerChosen = true;
                }
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
            } else if (!usernames.contains(nextUser)) {
                if (duelService.getById(duelStart).get().getPlayers().contains(user)) {
                    return -4L;
                } else {
                    return -5L;
                }
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

