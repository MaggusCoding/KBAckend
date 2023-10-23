package com.vocab.vocabulary_management.entities;

import java.util.List;

public class Flashcard {
    private Long flashCardId;
    private String originalText;
    private FlashcardList flashcardList;
    private List<Translation> translations;
}
