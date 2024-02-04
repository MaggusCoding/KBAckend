package com.vocab.application_ui_impl.controller;

import com.vocab.application_ui_impl.views.DuelView;
import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management.exceptions.UserNotExistException;
import com.vocab.user_management.services.UserService;
import com.vocab.user_management_impl.services.UserServiceImpl;
import com.vocab.vocabulary_duel_API.entities.Answer;
import com.vocab.vocabulary_duel_API.entities.Duel;
import com.vocab.vocabulary_duel_API.entities.Round;
import com.vocab.vocabulary_duel_API.exceptions.*;
import com.vocab.vocabulary_duel_API.services.DuelService;
import com.vocab.vocabulary_duel_impl.services.DuelServiceImpl;
import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management.exceptions.FlashcardListNotExistException;
import com.vocab.vocabulary_management.services.FlashcardListService;
import com.vocab.vocabulary_management_impl.services.FlashcardListServiceImpl;
import org.springframework.stereotype.Controller;

import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DuelControllerImpl {
    private final DuelService duelService;
    private final UserService userService;
    private final FlashcardListService flashcardListService;

    private final DuelView duelView;

    private Long currentPlayerOfDuel = 0L;

    public DuelControllerImpl(DuelServiceImpl duelService, UserServiceImpl userService, FlashcardListServiceImpl flashcardListService, DuelView duelView) {
        this.duelService = duelService;
        this.userService = userService;
        this.flashcardListService = flashcardListService;
        this.duelView = duelView;
    }

    public void createDuel(Long loggedInUser) {

        duelView.printAvailableFlashcardLists("Available Flashcard Lists:");
        List<FlashcardList> flashcardLists = flashcardListService.getAll();
        flashcardLists.forEach(flashcardList ->
                duelView.printAvailableFlashcardLists(flashcardList.getFlashcardListId() + " - " + flashcardList.getCategory()));

        Long flashcardListId = null;
        boolean optionInvalid = true;
        while (optionInvalid) {
            duelView.printCreateDuelInstruction();
            try {
                flashcardListId = duelView.readLong();
                Long finalFlashcardListId = flashcardListId;
                if (flashcardLists.stream().anyMatch(flashcardList -> flashcardList.getFlashcardListId().equals(finalFlashcardListId))) {
                    optionInvalid = false;
                } else if (flashcardListId.equals(0L)) {
                    return;
                } else {
                    duelView.printInputFailMessage();
                }
            } catch (InputMismatchException e) {
                duelView.printInputFailMessage();
                duelView.readString();
            }
        }

        try {
            Duel duel = duelService.createDuel(loggedInUser, flashcardListId);
            duelService.generateRounds(duel.getDuelId());
            duelView.printDuelCreated(duel.getDuelId());
        } catch (UserNotExistException e) {
            duelView.printUserNotExist2(e.getMessage());
        } catch (DuelNotExistException e) {
            duelView.printDuelNotExists(e.getMessage());
        } catch (FlashcardListNotExistException e) {
            duelView.printFlashcardListNotExists(e.getMessage());
        }
    }

    public void joinDuel(Long loggedInUser) {
        duelView.printAvailableDuelsToJoin("Select Duel to Join:");
        List<Duel> duelsToJoin = null;
        try {
            duelsToJoin = duelService.duelsToJoin(loggedInUser);
            duelsToJoin.forEach(duel ->
                    duelView.printAvailableDuelsToJoin(duel.getDuelId() + " - " + duel.getFlashcardsForDuel().getCategory()));

            Long duelId = null;
            boolean optionInvalid = true;
            while (optionInvalid) {
                duelView.printJoinInstruction();
                try {
                    duelId = duelView.readLong();
                    Long finalDuelId = duelId;
                    if (duelsToJoin.stream().anyMatch(duel -> duel.getDuelId().equals(finalDuelId))) {
                        optionInvalid = false;
                    } else if (duelId.equals(0L)) {
                        return;
                    } else {
                        duelView.printInputFailMessage();
                    }
                } catch (InputMismatchException e) {
                    duelView.printInputFailMessage();
                    duelView.readString();
                }
            }

            boolean success = duelService.joinDuel(duelId, loggedInUser);
            if (success) {
                duelView.printSuccessfulJoinedDuel();
            } else {
                duelView.printFailedJoiningDuel();
            }
        } catch (UserNotExistException e) {
            duelView.printUserNotExist2(e.getMessage());
        } catch (DuelNotExistException e) {
            duelView.printDuelNotExists(e.getMessage());
        } catch(DuelAlreadyStartedException e){
            duelView.printDuelAlreadyStarted(e.getMessage());
        } catch(UserAlreadyPartOfDuelException e) {
            duelView.printUserAlreadyPartOfDuel(e.getMessage());
        }
    }

    public void startDuel(Long loggedInUser) {
        try {
            List<Duel> duelsToStart = duelService.duelsToStart(loggedInUser);
            if (!duelsToStart.isEmpty()) {
                duelView.printAvailableDuelsToStart("Select Duel to Start:");
                duelsToStart.forEach(duel1 ->
                        duelView.printAvailableDuelsToStart(duel1.getDuelId() + " - " + duel1.getFlashcardsForDuel().getCategory()));
                boolean optionInvalid = true;
                Long duelStart = null;
                while (optionInvalid) {
                    duelView.printStartInstruction();
                    try {
                        duelStart = duelView.readLong();
                        Long finalDuelStart = duelStart;
                        if (duelsToStart.stream().anyMatch(duelTemp2 -> duelTemp2.getDuelId().equals(finalDuelStart))) {
                            optionInvalid = false;
                        } else if (duelStart.equals(0L)) {
                            return;
                        } else {
                            duelView.printInputFailMessage();
                        }
                    } catch (InputMismatchException e) {
                        duelView.printInputFailMessage();
                        duelView.readString();
                    }
                }
                duelService.startDuel(duelStart, loggedInUser);
                List<String> flashcardString = duelService.playRound(duelStart);
                currentPlayerOfDuel = loggedInUser;
                boolean duelFinished = false;
                for (int x = 1; x <= 10; x++) {
                    duelView.printCurrentRound(x);
                    // Player abort the round?
                    if (!playOneRound(flashcardString, duelStart)) {
                        break;
                    }
                    if (x == 10) {
                        duelFinished = true;
                    } else {
                        flashcardString = duelService.playRound(duelStart);
                        duelView.printEndOfRound();
                    }
                }
                if (duelFinished) {
                    duelView.printEndOfDuel();
                    String winners = duelService.calculateWinner(duelStart).stream().map(UserEntity::getUsername).collect(Collectors.joining(","));
                    if (winners.isEmpty()) {
                        duelView.printWinners("Everybody answered wrong! Nobody wins!");
                    } else {
                        duelView.printWinners("The winner is/are " + winners);
                    }
                }
            } else {
                duelView.printNoJoinableDuel();
            }
        } catch(UserNotExistException e){
            duelView.printUserNotExist2(e.getMessage());
        } catch (DuelNotExistException e) {
            duelView.printDuelNotExists(e.getMessage());
        } catch (UserNotPartOfDuelException e) {
            duelView.printUserNotParticipating2(e.getMessage());
        } catch (DuelAlreadyStartedException e) {
            duelView.printDuelAlreadyStarted(e.getMessage());
        }
    }

    public void deleteDuel() {
        duelView.printAvailableDuelsToDelete("Available Duels:");
        List<Duel> duels = duelService.getAll();
        duels.forEach(duel ->
                duelView.printAvailableDuelsToDelete(duel.getDuelId() + " - " + duel.getFlashcardsForDuel().getCategory()));

        duelView.printDeleteInstruction();
        Long duelId = null;
        boolean optionInvalid = true;
        while (optionInvalid) {
            try {
                duelId = duelView.readLong();
                Long finalDuelId = duelId;
                if (duels.stream().anyMatch(duel -> duel.getDuelId().equals(finalDuelId))) {
                    optionInvalid = false;
                } else if (duelId.equals(0L)) {
                    return;
                } else {
                    duelView.printInputFailMessage();
                }
            } catch (InputMismatchException e) {
                duelView.printInputFailMessage();
                duelView.readString(); // Consume the invalid input
            } catch (Exception e) {
                duelView.printErrorMessage("Error: " + e.getMessage());
            }
        }
        try{
            duelService.deleteDuel(duelId);
            duelView.printDuelDeleted();
        } catch(DuelNotExistException e){
            duelView.printDuelNotExists(e.getMessage());
        }
    }

    private boolean playOneRound(List<String> flashcardString, Long duelStart) {
        boolean allUsersPlayed = false;
        try {
            while (!allUsersPlayed) {
                duelView.printCurrentPlayer(userService.getById(currentPlayerOfDuel).getUsername());
                duelView.printQuestion(flashcardString.get(0));

                for (int i = 1; i < 5; i++) {
                    duelView.printAnswer(i + "." + flashcardString.get(i));
                }
                int selectedAnswer = 0;
                boolean optionInvalid = true;
                while (optionInvalid) {
                    try {
                        selectedAnswer = duelView.readInt();
                        if (0 < selectedAnswer && selectedAnswer < 5) {
                            optionInvalid = false;
                        } else {
                            duelView.printInputFailMessage();
                        }
                    } catch (InputMismatchException e) {
                        duelView.printInputFailMessage();
                        duelView.readString();
                    }
                }
                duelView.printAnswer("Your answer: " + flashcardString.get(selectedAnswer));
                try {
                    duelService.saveSelectedAnswer(flashcardString.get(selectedAnswer), duelStart, currentPlayerOfDuel);
                } catch(UserAlreadyPlayedRoundException e){
                    duelView.printUserAlreadyPlayed2(e.getMessage());
                }

                boolean nextPlayerChosen = false;
                while (!nextPlayerChosen) {
                    Long nextUser = printChooseNextPlayer(duelStart);
                    switch (nextUser.intValue()) {
                        case -1:
                            duelView.printAllAnswered();
                            allUsersPlayed = true;
                            nextPlayerChosen = true;
                            break;
                        case -2:
                            // Typed exit
                            return false;
                        case -3:
                            duelView.printUserNotExist();
                            break;
                        case -4:
                            duelView.printUserAlreadyPlayed();
                            break;
                        case -5:
                            duelView.printUserNotParticipating();
                            break;
                        default:
                            currentPlayerOfDuel = nextUser;
                            nextPlayerChosen = true;
                    }
                }
            }
            duelService.activateNextRound(duelStart);
            return true;
        } catch(UserNotExistException e){
            duelView.printUserNotExist2(e.getMessage());
            return false;
        } catch (DuelNotExistException e) {
            duelView.printDuelNotExists(e.getMessage());
            return false;
        }
    }

    private Long printChooseNextPlayer(Long duelStart) {
        List<String> usernames = getUsersWhoNotPlayed(duelStart);
        if (usernames.isEmpty()) {
            return -1L;
        } else {
            duelView.printNextPlayer();
            duelView.printAvailableNextPlayer(usernames.stream().collect(Collectors.joining(System.lineSeparator())));
            String nextUser = duelView.readString();
            try {
                if (nextUser.equalsIgnoreCase("exit")) {
                    return -2L;
                }
                UserEntity user = userService.findByUsername(nextUser);
                if (!usernames.contains(nextUser)) {
                    if (duelService.getById(duelStart).get().getPlayers().contains(user)) {
                        return -4L;
                    }
                    return -5L;
                }
                return user.getUserId();
            } catch (UserNotExistException e) {
                return -3L;
            }
        }
    }

    private List<String> getUsersWhoNotPlayed(Long duelStart) {
        Duel duel = duelService.getById(duelStart).get();
        Round activeRound = duel.getRounds().stream().filter(Round::isActiveRound).findFirst().get();
        List<UserEntity> usersAlreadyPlayed = activeRound.getSelectedAnswers().stream().map(Answer::getPlayer).toList();
        List<UserEntity> players = duelService.getById(duelStart).get().getPlayers();
        if (usersAlreadyPlayed.size() == players.size()) {
            return List.of();
        }
        return players.stream().filter(player -> !usersAlreadyPlayed.contains(player)).map(UserEntity::getUsername).collect(Collectors.toList());
    }

    // Weitere Methoden für zusätzliche Duell-Aktionen können hier hinzugefügt werden
}
