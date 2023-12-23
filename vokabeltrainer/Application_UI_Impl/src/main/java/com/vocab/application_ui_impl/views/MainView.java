package com.vocab.application_ui_impl.views;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MainView {

    private final Scanner scanner = new Scanner(System.in);

    public int readInt() {
        return scanner.nextInt();
    }

    public String readString(){
        return scanner.next();
    }

    public void printMainMenu() {
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
    }

    public void printToLoginMessage() {
        System.out.println("Please retrieve/login a user.");
    }

    public void printInvalidChoice() {
        System.out.println("Invalid choice. Please enter a number between 0 and 8.");
    }

    public void printInvalidInput() {
        System.out.println("Invalid input. Please enter a number.");
    }

    public void printGoodBye() {
        System.out.println("Have a nice day!");
    }
}
