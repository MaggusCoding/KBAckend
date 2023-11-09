package com.vocab.vocabulary_management.services;

import com.vocab.vocabulary_management.entities.Translation;

import java.util.List;

public interface TranslationService {
    /**
     * Create a new translation
     * @param translation The translation to be created
     * @return The created translation
     */
    Translation createTranslation(Translation translation);

    /**
     * Gets a translation by id
     * @param id The id of the translation
     * @return The translation with the given id
     */
    Translation getById(Long id);

    /**
     * Gets all translations of a flashcard
     * @param flashcardId The id of the flashcard which the translations belongs to
     * @return A list of all translations for the given flashcardId
     */
    List<Translation> getAllTranslationsByFlashcard(Long flashcardId);

}