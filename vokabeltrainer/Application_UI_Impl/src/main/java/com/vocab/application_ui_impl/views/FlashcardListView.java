package com.vocab.application_ui_impl.views;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class FlashcardListView {

    private final Scanner scanner = new Scanner(System.in);

    public Long readLong() {
        return scanner.nextLong();
    }

    public int readInt() {
        return scanner.nextInt();
    }

    public String readString(){
        return scanner.next();
    }

    public void printMenu() {
        System.out.println("0. Go back");
        System.out.println("1. Import new FlashcardList");
        System.out.println("2. Import initial FlashcardLists");
        System.out.println("3. Delete a FlashcardList");
        System.out.println("Enter your choice: ");
    }

    public void printInputFailMessage() {
        System.out.println("Entered ID is invalid. Try again!");
    }

    public void printImportFileInstruction() {
        System.out.println("Enter an absolute filepath: ");
    }

    public void printImportFileSuccess() {
        System.out.println("Importing new Flashcardlist was successful.");
    }

    public void printImportFileFail() {
        System.out.println("Importing Flashcardlist was unsuccessful. The file might be empty.");
    }

    public void printNoFileFound(String path) {
        System.out.println("No file found at the given path. Path: " + path);
    }

    public void printError() {
        System.out.println("Something went wrong. Contact your administrator.");
    }

    public void printImportInitialFlashcardsSuccess() {
        System.out.println("initial Flashcards successfully imported.");
    }

    public void printImportInitialFlashcardsFail() {
        System.out.println("Couldn´t import initial flashcardlists. Maybe default path is empty.");
    }

    public void printNoInitialFilesFound() {
        System.out.println("No files found at the default path.");
    }

    public void printAvailableFlashcardLists(String msg) {
        System.out.println(msg);
    }

    public void printDeleteFlashcardListInstruction() {
        System.out.println("Enter the id of the FlashcardList to delete or '0' to go back: ");
    }

    public void printDeleteFlashcardListSuccessful(Long id) {
        System.out.println("Deletion of Flashcardlist with ID " + id + " was successful.");
    }

    public void printDeletionFailedFlashcardInUse(Long id) {
        System.out.println("FlashcardList with ID " + id + " couldn´t be deleted. It is used in a duel. Delete the duel first.");
    }

    public void printDeletionFailedFlashcardNotExists(Long id) {
        System.out.println("FlashcardList with ID " + id + " couldn´t be deleted. It does not exist.");
    }

    public void printError(String message) {
        System.out.println(message);
    }
}
