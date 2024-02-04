package com.vocab.vocabulary_management.services;

import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management.exceptions.ContentEmptyException;
import com.vocab.vocabulary_management.exceptions.FlashcardListNotExistException;
import com.vocab.vocabulary_management.exceptions.FlashcardListStillInUseException;

import java.util.List;

public interface FlashcardListService {
    
    /**
     * Create new flashcard list with given content.
     *
     * @param content content of flashcardlist to be saved
     * @return true if succeeded, false otherwise
     * @throws ContentEmptyException if the content is empty
     */
    Boolean createFlashcardList(String content) throws ContentEmptyException;

    /**
     * Gets a flashcard list by id
     * @param id The ID of the flashcard list to be retrieved
     * @return The flashcard list with the given ID
     * @throws FlashcardListNotExistException if the flashcardList not exists
     */
    FlashcardList getById(Long id) throws FlashcardListNotExistException;

    /**
     * Gets all flashcard lists
     * @return All flashcard lists
     */
    List<FlashcardList> getAll();

    /**
     * deletes a flashcardlist if it is not referenced by a duel.
     * @param id of flashcardlist
     * @return true if succeeded, false otherwise
     * @throws FlashcardListNotExistException if the flashcardlist not exists
     * @throws FlashcardListStillInUseException if the flashcardlist is used in a duel
     */
    boolean deleteFlashcardList(Long id) throws FlashcardListNotExistException, FlashcardListStillInUseException;
}


