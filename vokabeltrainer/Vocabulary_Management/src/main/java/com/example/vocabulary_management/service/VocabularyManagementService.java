package com.example.vocabulary_management.service;

import com.example.vocabulary_management.entities.Flashcard;

public interface VocabularyManagementService {

    Flashcard fetchFlashcard(int flashCardId);
    boolean importFlashcard();
}
