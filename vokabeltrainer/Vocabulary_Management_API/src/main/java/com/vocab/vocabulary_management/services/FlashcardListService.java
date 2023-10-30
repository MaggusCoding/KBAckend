package com.vocab.vocabulary_management.services;

import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.FlashcardList;

import java.io.FileNotFoundException;
import java.nio.charset.MalformedInputException;
import java.util.List;

public interface FlashcardListService {
    /**
     * Create a new flashcard lists from default filepath. Optional filename to import specific file.
     *
     * @param filename optional if a specific file should be imported
     * @return The created flashcard list
     */
    Boolean createFlashcardList(String filename) throws FileNotFoundException, MalformedInputException;

    /**
     * Gets a flashcard list by id
     * @param id The ID of the flashcard list to be retrieved
     * @return The flashcard list with the given ID
     */
    FlashcardList getById(Long id);

    /**
     * Gets all flashcard lists
     * @return All flashcard lists
     */
    List<FlashcardList> getAll();

    /**
     * Gets all Flashcards in a FlashcardList
     * @param id The ID of the FlashcardList
     * @return All Flashcards in the FlashcardList
     */
    List<Flashcard> getFlashcardsByFlashcardListId(Long id);
}


