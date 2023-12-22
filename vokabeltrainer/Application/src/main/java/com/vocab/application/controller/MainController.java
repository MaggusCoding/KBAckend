package com.vocab.application.controller;

import org.springframework.stereotype.Controller;

import java.util.InputMismatchException;
import java.util.Scanner;

@Controller
public class MainController {

    private final UserController userController;
    private final DuelController duelController;
    private final FlashcardListController flashcardListController;
    private final DatabaseController databaseController;

    private Long loggedInUser = null;

    public MainController(UserController userController, DuelController duelController,
                          FlashcardListController flashcardListController,
                          DatabaseController databaseController) {
        this.userController = userController;
        this.duelController = duelController;
        this.flashcardListController = flashcardListController;
        this.databaseController = databaseController;
        userController.initializeDefaultUser();
    }

    public void displayMainMenu(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nMain Menu:");
            System.out.println("0. Clear DB (for convenience)");
            System.out.println("1. Create/Retrieve a Player");
            System.out.println("2. Delete a player");
            System.out.println("3. Create Duel");
            System.out.println("4. Join Existing Duel");
            System.out.println("5. Start Duel");
            System.out.println("6. Delete a Duel");
            System.out.println("7. Import/Delete FlashcardList");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            int choice = -1;
            try {
                choice = scanner.nextInt();
                switch (choice) {
                    case 0:
                        databaseController.clearDatabase(scanner);
                        break;
                    case 1:
                        loggedInUser = userController.createUser(scanner);
                        break;
                    case 2:
                        if (loggedInUser != null) {
                            userController.deleteUser(scanner, loggedInUser);
                        } else {
                            System.out.println("Please retrieve a user.");
                        }
                        break;
                    case 3:
                        if (loggedInUser != null) {
                            duelController.createDuel(scanner, loggedInUser);
                        } else {
                            System.out.println("Please retrieve a user.");
                        }
                        break;
                    case 4:
                        if (loggedInUser != null) {
                            duelController.joinDuel(scanner, loggedInUser);
                        } else {
                            System.out.println("Please retrieve a user.");
                        }
                        break;
                    case 5:
                        if (loggedInUser != null) {
                            duelController.startDuel(scanner, loggedInUser);
                        } else {
                            System.out.println("Please retrieve a user.");
                        }
                        break;
                    case 6:
                        duelController.deleteDuel(scanner);
                        break;
                    case 7:
                        flashcardListController.manageFlashcardList(scanner);
                        break;
                    case 8:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 0 and 8.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // clear the incorrect input
            }
        }
        System.out.println("Have a nice day!");
        System.exit(0);
    }
}


