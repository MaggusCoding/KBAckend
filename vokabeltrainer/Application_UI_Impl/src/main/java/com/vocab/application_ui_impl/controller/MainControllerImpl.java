package com.vocab.application_ui_impl.controller;

import com.vocab.application_ui_api.controller.MainController;
import com.vocab.application_ui_impl.views.MainView;
import org.springframework.stereotype.Controller;

import java.util.InputMismatchException;

@Controller
public class MainControllerImpl implements MainController {

    private final UserControllerImpl userController;
    private final DuelControllerImpl duelController;
    private final FlashcardListControllerImpl flashcardListController;
    private final DatabaseControllerImpl databaseController;

    private final MainView mainView;

    private Long loggedInUser = null;

    public MainControllerImpl(UserControllerImpl userController, DuelControllerImpl duelController,
                              FlashcardListControllerImpl flashcardListController,
                              DatabaseControllerImpl databaseController, MainView mainView) {
        this.userController = userController;
        this.duelController = duelController;
        this.flashcardListController = flashcardListController;
        this.databaseController = databaseController;
        this.mainView = mainView;
        userController.initializeDefaultUser();
    }

    public void displayMainMenu() {
        boolean exit = false;
        while (!exit) {
            mainView.printMainMenu();

            int choice = -1;
            try {
                choice = mainView.readInt();
                switch (choice) {
                    case 0:
                        databaseController.clearDatabase();
                        break;
                    case 1:
                        loggedInUser = userController.createUser();
                        break;
                    case 2:
                        if (loggedInUser != null) {
                            userController.deleteUser(loggedInUser);
                        } else {
                            mainView.printToLoginMessage();
                        }
                        break;
                    case 3:
                        if (loggedInUser != null) {
                            duelController.createDuel(loggedInUser);
                        } else {
                            mainView.printToLoginMessage();
                        }
                        break;
                    case 4:
                        if (loggedInUser != null) {
                            duelController.joinDuel(loggedInUser);
                        } else {
                            mainView.printToLoginMessage();
                        }
                        break;
                    case 5:
                        if (loggedInUser != null) {
                            duelController.startDuel(loggedInUser);
                        } else {
                            mainView.printToLoginMessage();
                        }
                        break;
                    case 6:
                        duelController.deleteDuel();
                        break;
                    case 7:
                        flashcardListController.manageFlashcardList();
                        break;
                    case 8:
                        exit = true;
                        break;
                    default:
                        mainView.printInvalidChoice();
                }
            } catch (InputMismatchException e) {
                mainView.printInvalidInput();
                mainView.readString(); // clear the incorrect input
            }
        }
        mainView.printGoodBye();
        System.exit(0);
    }
}


