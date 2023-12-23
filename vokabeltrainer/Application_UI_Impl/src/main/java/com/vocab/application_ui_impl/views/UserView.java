package com.vocab.application_ui_impl.views;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class UserView {

    private final Scanner scanner = new Scanner(System.in);

    public Long readLong() {
        return scanner.nextLong();
    }

    public String readString(){
        return scanner.next();
    }

    public void printInputFailMessage() {
        System.out.println("Entered ID is invalid. Try again!");
    }

    public void printErrorMessage(String msg) {
        System.out.println(msg);
    }

    public void printUserExistsInADuel() {
        System.out.println("User deletion was unsuccessful. The user participates in a duel. Delete the duel first.");
    }

    public void printUserDoesNotExist() {
        System.out.println("User deletion was unsuccessful. The user does not exist.");
    }

    public void printAvailableUsers(String user) {
        System.out.println(user);
    }

    public void printDeleteInstruction() {
        System.out.println("Enter the ID of the user to delete or '0' to go back:");
    }

    public void printCreateInstruction() {
        System.out.println("Enter the username to create the new user or log in as the user:");
    }

    public void printLoginMessage(Long userId) {
        System.out.println("Logged in with user ID: " + userId);
    }

    public void printDeletionSuccess() {
        System.out.println("User deleted successfully.");
    }
}
