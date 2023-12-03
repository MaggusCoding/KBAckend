package com.vocab.vocabulary_management.services;

import com.vocab.vocabulary_management.entities.Flashcard;

// TODO: Überprüfen, ob noch gebraucht
public interface FlashcardService {
    /**
     * Create a new flashcard
     * @param flashcard The flashcard to be created
     * @return The created flashcard
     */
    Flashcard createFlashcard(Flashcard flashcard);

    /**
     * Gets a flashcard by id
     * @param id The id of the flashcard
     * @return  The flashcard with the given id
     */
    Flashcard getById(Long id);

}
