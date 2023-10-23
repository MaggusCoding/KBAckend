package com.vocab.vocabulary_management.services;

import com.vocab.vocabulary_management.entities.Flashcard;

import java.util.List;

public interface FlashcardService {

    Flashcard createFlashcard(Flashcard flashcard);

    Flashcard getById(Long id);

    List<Flashcard> getAll();

    List<Synonym> getSynonymesByFlashcardId(Long id);

}
