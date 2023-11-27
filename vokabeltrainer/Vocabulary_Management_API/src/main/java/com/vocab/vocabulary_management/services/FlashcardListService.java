package com.vocab.vocabulary_management.services;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.FlashcardList;


import java.io.FileNotFoundException;
import java.nio.charset.MalformedInputException;
import java.util.List;

public interface FlashcardListService {
    

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


}


