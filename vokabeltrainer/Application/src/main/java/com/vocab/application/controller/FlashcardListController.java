package com.vocab.application.controller;

import com.vocab.application.serviceImpl.ImportServiceImpl;
import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management_impl.services.FlashcardListServiceImpl;
import org.springframework.stereotype.Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
@Controller
public class FlashcardListController {
    private final FlashcardListServiceImpl flashcardListService;
    private final ImportServiceImpl importService;

    public FlashcardListController(FlashcardListServiceImpl flashcardListService, ImportServiceImpl importService) {
        this.flashcardListService = flashcardListService;
        this.importService = importService;
    }

    public void manageFlashcardList(Scanner scanner) {
        System.out.println("1. Import new FlashcardList");
        System.out.println("2. Import initial FlashcardLists");
        System.out.println("3. Delete a FlashcardList");
        System.out.println("Enter your choice: ");

        boolean optionInvalid = true;
        int option = 0;
        while (optionInvalid) {
            try {
                option = scanner.nextInt();
                if (0 < option && option < 4) {
                    optionInvalid = false;
                } else {
                    System.out.println("Entered ID is invalid. Try again!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entered ID is invalid. Try again!");
                scanner.next();
            }
        }
        switch (option) {
            case 1:
                System.out.println("Enter an absolute filepath: ");
                String path = scanner.next();
                try {
                    if (importService.importFile(path)) {
                        System.out.println("Importing new Flashcardlist was successful.");
                    }
                } catch (FileNotFoundException fex) {
                    System.out.println("Zu dem angegebenen Pfad existiert keine Datei. Pfad: " + path);
                } catch (Exception ex) {
                    System.out.println("Etwas ist schief gelaufen. Kontaktieren Sie ihren Administrator.");
                }
                break;
            case 2:
                try {
                    boolean importSuccess = importService.importInitialFiles();
                    if (importSuccess) {
                        System.out.println("initiale Flashcards wurden importiert.");
                    }
                } catch (IOException ioex) {
                    if (ioex instanceof FileNotFoundException) {
                        System.out.println("Zu dem angegebenen Pfad existiert keine Datei. Fehlernachricht: " + ioex.getMessage());
                    } else {
                        System.out.println("Etwas ist schief gelaufen. Kontaktieren Sie ihren Administrator.");
                    }
                }
                break;
            case 3:
                System.out.println("Existing FlashcardLists: ");
                List<FlashcardList> flashcardListLists = flashcardListService.getAll();
                flashcardListLists.forEach(flashcardList ->
                        System.out.println(flashcardList.getFlashcardListId() + " - " + flashcardList.getCategory() + " -- " + flashcardList.getOriginalLanguage() + " - " + flashcardList.getTranslationLanguage()));
                System.out.println("Enter the id of the FlashcardList to delete: ");
                Long id = scanner.nextLong();
                boolean successDelete = flashcardListService.deleteFlashcardList(id);
                if (successDelete) {
                    System.out.println("Deletion of Flashcardlist with ID " + id + " was successful.");
                } else {
                    if (flashcardListLists.stream().anyMatch(flashcardList -> flashcardList.getFlashcardListId().equals(id))) {
                        System.out.println("FlashcardList with ID " + id + " couldn´t be deleted. It is used in a duel. Delete the duel first.");
                    } else {
                        System.out.println("FlashcardList with ID " + id + " couldn´t be deleted. It does not exist.");
                    }
                }
        }
    }

    private void importFlashcardList(Scanner scanner) {
        System.out.print("Enter the file path for the FlashcardList to import: ");
        scanner.nextLine(); // Consume newline left-over
        String path = scanner.nextLine();
        try {
            if (importService.importFile(path)) {
                System.out.println("Importing new Flashcardlist was successful.");
            }
        } catch (FileNotFoundException fex) {
            System.out.println("No file found at the given path. Path: " + path);
        } catch (Exception ex) {
            System.out.println("Something went wrong. Please contact your administrator. Error: " + ex.getMessage());
        }
    }

    private void deleteFlashcardList(Scanner scanner) {
        System.out.println("Available FlashcardLists:");
        List<FlashcardList> flashcardLists = flashcardListService.getAll();
        flashcardLists.forEach(flashcardList ->
                System.out.println(flashcardList.getFlashcardListId() + " - " + flashcardList.getCategory()));

        System.out.print("Enter the ID of the FlashcardList to delete: ");
        try {
            Long flashcardListId = scanner.nextLong();
            flashcardListService.deleteFlashcardList(flashcardListId);
            System.out.println("FlashcardList deleted successfully.");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid FlashcardList ID.");
            scanner.next(); // Consume the invalid input
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Weitere Methoden für zusätzliche FlashcardList-Aktionen können hier hinzugefügt werden
}

