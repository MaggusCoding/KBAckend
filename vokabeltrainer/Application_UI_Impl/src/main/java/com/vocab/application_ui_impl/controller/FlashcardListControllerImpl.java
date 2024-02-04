package com.vocab.application_ui_impl.controller;

import com.vocab.application_ui_impl.serviceImpl.ImportServiceImpl;
import com.vocab.application_ui_impl.views.FlashcardListView;
import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management.exceptions.ContentEmptyException;
import com.vocab.vocabulary_management.exceptions.FlashcardListNotExistException;
import com.vocab.vocabulary_management.exceptions.FlashcardListStillInUseException;
import com.vocab.vocabulary_management_impl.services.FlashcardListServiceImpl;
import org.springframework.stereotype.Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;

@Controller
public class FlashcardListControllerImpl {
    private final FlashcardListServiceImpl flashcardListService;
    private final ImportServiceImpl importService;

    private final FlashcardListView flashcardListView;

    public FlashcardListControllerImpl(FlashcardListServiceImpl flashcardListService, ImportServiceImpl importService, FlashcardListView flashcardListView) {
        this.flashcardListService = flashcardListService;
        this.importService = importService;
        this.flashcardListView = flashcardListView;
    }

    public void manageFlashcardList() {

        flashcardListView.printMenu();

        boolean optionInvalid = true;
        int option = 0;
        while (optionInvalid) {
            try {
                option = flashcardListView.readInt();
                if (0 <= option && option < 4) {
                    optionInvalid = false;
                } else {
                    flashcardListView.printInputFailMessage();
                }
            } catch (InputMismatchException e) {
                flashcardListView.printInputFailMessage();
                flashcardListView.readString();
            }
        }
        switch (option) {
            case 0:
                break;
            case 1:
                flashcardListView.printImportFileInstruction();
                String path = flashcardListView.readString();
                try {
                    if (importService.importFile(path)) {
                        flashcardListView.printImportFileSuccess();
                    } else {
                        flashcardListView.printImportFileFail();
                    }
                } catch (FileNotFoundException fex) {
                    flashcardListView.printNoFileFound(path);
                } catch (Exception ex) {
                    flashcardListView.printError();
                }
                break;
            case 2:
                try {
                    boolean importSuccess = importService.importInitialFiles();
                    if (importSuccess) {
                        flashcardListView.printImportInitialFlashcardsSuccess();
                    } else{
                        flashcardListView.printImportInitialFlashcardsFail();
                    }
                } catch (IOException ioex) {
                    if (ioex instanceof FileNotFoundException) {
                        flashcardListView.printNoInitialFilesFound();
                    } else {
                        flashcardListView.printError();
                    }
                } catch (ContentEmptyException ex){
                    flashcardListView.printError(ex.getMessage());
                }
                break;
            case 3:
                flashcardListView.printAvailableFlashcardLists("Existing FlashcardLists: ");
                List<FlashcardList> flashcardListLists = flashcardListService.getAll();
                flashcardListLists.forEach(flashcardList ->
                        flashcardListView.printAvailableFlashcardLists(flashcardList.getFlashcardListId() + " - " + flashcardList.getCategory() + " -- " + flashcardList.getOriginalLanguage() + " - " + flashcardList.getTranslationLanguage()));
                flashcardListView.printDeleteFlashcardListInstruction();
                Long id = 0L;
                optionInvalid = true;
                while (optionInvalid) {
                    try {
                        id = flashcardListView.readLong();
                        Long finalId1 = id;
                        if (flashcardListLists.stream().anyMatch(flashcardList -> flashcardList.getFlashcardListId().equals(finalId1)) || finalId1.equals(0L)) {
                            optionInvalid = false;
                        } else {
                            flashcardListView.printInputFailMessage();
                        }
                    } catch (InputMismatchException e) {
                        flashcardListView.printInputFailMessage();
                        flashcardListView.readString();
                    }
                }
                if (id > 0) {
                    try{
                        flashcardListService.deleteFlashcardList(id);
                        flashcardListView.printDeleteFlashcardListSuccessful(id);
                    } catch(FlashcardListNotExistException e) {
                        flashcardListView.printDeletionFailedFlashcardNotExists(id);
                    } catch(FlashcardListStillInUseException e) {
                        flashcardListView.printDeletionFailedFlashcardInUse(id);
                    }
                }
        }
    }

    // Weitere Methoden für zusätzliche FlashcardList-Aktionen können hier hinzugefügt werden
}

