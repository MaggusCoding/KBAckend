package com.vocab.vocabulary_management.services;

import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.FlashcardList;
import java.util.List;

public interface FlashcardListService {

    FlashcardList createFlashcardList(FlashcardList flashcardList);

    FlashcardList getById(Long id);

    List<FlashcardList> getAll();

    List<Flashcard> getFlashcardsByFlashcardListId(Long id);
}


