package com.vocab.application.controller;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management_impl.services.UserServiceImpl;
import com.vocab.vocabulary_duel.entities.Answer;
import com.vocab.vocabulary_duel.entities.Duel;
import com.vocab.vocabulary_duel.entities.Round;
import com.vocab.vocabulary_duel_impl.services.DuelServiceImpl;
import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management_impl.services.FlashcardListServiceImpl;
import org.springframework.stereotype.Controller;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Controller
public class DuelController {
    private final DuelServiceImpl duelService;
    private final UserServiceImpl userService;
    private final FlashcardListServiceImpl flashcardListService;

    private Long loggendInUser = 1L;

    public DuelController(DuelServiceImpl duelService, UserServiceImpl userService, FlashcardListServiceImpl flashcardListService) {
        this.duelService = duelService;
        this.userService = userService;
        this.flashcardListService = flashcardListService;
        this.loggendInUser = userService.createUser("god").getUserId();
    }

    public void createDuel(Scanner scanner) {
        System.out.println("Select User:");
        List<UserEntity> users = userService.getAll();
        users.forEach(user -> System.out.println(user.getUserId() + " - " + user.getUsername()));

        Long userIdDuel = null;
        boolean optionInvalid = true;
        while (optionInvalid) {
            System.out.print("Enter ID of the user: ");
            try {
                userIdDuel = scanner.nextLong();
                Long finalUserIdDuel = userIdDuel;
                if (users.stream().anyMatch(user -> user.getUserId().equals(finalUserIdDuel))) {
                    optionInvalid = false;
                } else {
                    System.out.println("Entered ID is invalid. Try again!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entered ID is invalid. Try again!");
                scanner.next();
            }
        }

        System.out.println("Available Flashcard Lists:");
        List<FlashcardList> flashcardLists = flashcardListService.getAll();
        flashcardLists.forEach(flashcardList ->
                System.out.println(flashcardList.getFlashcardListId() + " - " + flashcardList.getCategory()));

        Long flashcardListId = null;
        optionInvalid = true;
        while (optionInvalid) {
            System.out.print("Enter the ID of the Flashcard List for the duel: ");
            try {
                flashcardListId = scanner.nextLong();
                Long finalFlashcardListId = flashcardListId;
                if (flashcardLists.stream().anyMatch(flashcardList -> flashcardList.getFlashcardListId().equals(finalFlashcardListId))) {
                    optionInvalid = false;
                } else {
                    System.out.println("Entered ID is invalid. Try again!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entered ID is invalid. Try again!");
                scanner.next();
            }
        }

        Duel duel = duelService.createDuel(userIdDuel, flashcardListId);
        duelService.generateRounds(duel.getDuelId());
        System.out.println("Duel created with ID: " + duel.getDuelId());
    }

    public void joinDuel(Scanner scanner) {
        System.out.println("Select Duel to Join:");
        List<Duel> duelsToJoin = duelService.duelsToJoin();
        duelsToJoin.forEach(duel ->
                System.out.println(duel.getDuelId() + " - " + duel.getFlashcardsForDuel().getCategory()));

        Long duelId = null;
        boolean optionInvalid = true;
        while (optionInvalid) {
            System.out.print("Enter the ID of the Duel to Join: ");
            try {
                duelId = scanner.nextLong();
                Long finalDuelId = duelId;
                if (duelsToJoin.stream().anyMatch(duel -> duel.getDuelId().equals(finalDuelId))) {
                    optionInvalid = false;
                } else {
                    System.out.println("Entered ID is invalid. Try again!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entered ID is invalid. Try again!");
                scanner.next();
            }
        }

        boolean success = duelService.joinDuel(duelId, loggendInUser);
        if (success) {
            System.out.println("Successfully joined Duel");
        } else {
            System.out.println("Error joining Duel. It might be already full or started.");
        }
    }

    public void startDuel(Scanner scanner) {
        List<Duel> duelsToStart = duelService.duelsToStart(loggendInUser);
        if (!duelsToStart.isEmpty()) {
            System.out.println("Select Duel to Start:");
            duelsToStart.forEach(duel1 ->
                    System.out.println(duel1.getDuelId() + " - " + duel1.getFlashcardsForDuel().getCategory()));
            boolean optionInvalid = true;
            Long duelStart = null;
            while (optionInvalid) {
                System.out.println("Enter the ID of the Duel to start:");
                try {
                    duelStart = scanner.nextLong();
                    Long finalDuelStart = duelStart;
                    if (duelsToStart.stream().anyMatch(duelTemp2 -> duelTemp2.getDuelId().equals(finalDuelStart))) {
                        optionInvalid = false;
                    } else {
                        System.out.println("Entered ID is invalid. Try again!");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Entered ID is invalid. Try again!");
                    scanner.next();
                }
            }
            duelService.startDuel(duelStart);
            List<String> flashcardString = duelService.playRound(duelStart);
            boolean duelFinished = false;
            for (int x = 1; x <= 10; x++) {
                System.out.println("#################### round " + x + "/10 ####################");
                // Player abort the round
                if (!playOneRound(flashcardString, scanner, duelStart)) {
                    break;
                }
                if (x == 10) {
                    duelFinished = true;
                } else {
                    flashcardString = duelService.playRound(duelStart);
                    System.out.println("Let´s begin the next round!");
                }
            }
            if (duelFinished) {
                System.out.println("All rounds were played. Let´s see who wins! ");
                String winners = duelService.calculateWinner(duelStart).stream().map(UserEntity::getUsername).collect(Collectors.joining(","));
                if (winners.isEmpty()) {
                    System.out.println("Everybody answered wrong! Nobody wins!");
                } else {
                    System.out.println("The winner is/are " + winners);
                }
            }
        } else {
            System.out.println("Apparently you didn´t join a duel.");
        }
    }

    public void deleteDuel(Scanner scanner) {
        System.out.println("Available Duels:");
        List<Duel> duels = duelService.getAll();
        duels.forEach(duel ->
                System.out.println(duel.getDuelId() + " - " + duel.getFlashcardsForDuel().getCategory()));

        System.out.print("Enter the ID of the Duel to delete: ");
        Long duelId = null;
        try {
            duelId = scanner.nextLong();
            duelService.deleteDuel(duelId);
            System.out.println("Duel deleted successfully.");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid Duel ID.");
            scanner.next(); // Consume the invalid input
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private boolean playOneRound(List<String> flashcardString, Scanner scanner, Long duelStart) {
        boolean allUsersPlayed = false;
        while (!allUsersPlayed) {
            System.out.println("#################### You are now " + userService.getById(loggendInUser).getUsername() + " ####################");
            System.out.println("What is the translation for: " + flashcardString.get(0) + "? (Enter the id of the answer.)");

            for (int i = 1; i < 5; i++) {
                System.out.println(i + "." + flashcardString.get(i));
            }
            int selectedAnswer = 0;
            boolean optionInvalid = true;
            while (optionInvalid) {
                try {
                    selectedAnswer = scanner.nextInt();
                    if (0 < selectedAnswer && selectedAnswer < 5) {
                        optionInvalid = false;
                    } else {
                        System.out.println("Entered ID is invalid. Try again!");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Entered ID is invalid. Try again!");
                    scanner.next();
                }
            }
            System.out.println("Your answer: " + flashcardString.get(selectedAnswer));
            duelService.saveSelectedAnswer(flashcardString.get(selectedAnswer), duelStart, loggendInUser);

            boolean nextPlayerChosen = false;
            while (!nextPlayerChosen) {
                Long nextUser = printChooseNextPlayer(duelStart, scanner);
                switch (nextUser.intValue()) {
                    case -1:
                        System.out.println("All players played this round.");
                        allUsersPlayed = true;
                        nextPlayerChosen = true;
                        break;
                    case -2:
                        System.out.println("Typed username does not exist. Choose other player!");
                        break;
                    case -3:
                        // Typed exit
                        return false;
                    case -4:
                        System.out.println("This user already played this round! Choose other player!");
                        break;
                    case -5:
                        System.out.println("This user is not participating in the current duel! Choose other player!");
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
            System.out.println("Which user should answer next? (Enter username or type exit to leave)");
            System.out.println(usernames.stream().collect(Collectors.joining(System.lineSeparator())));
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

    // Weitere Methoden für zusätzliche Duell-Aktionen können hier hinzugefügt werden
}
