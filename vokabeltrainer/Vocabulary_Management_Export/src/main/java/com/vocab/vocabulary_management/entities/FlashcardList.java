package com.vocab.vocabulary_management.entities;

import java.util.List;

public class FlashcardList {

    private Long id;

    private String category;

    private String originalLanguage;

    private String translationLanguage;

    private List<Flashcard> flashcards;

    public FlashcardList(Long id, String category, String originalLanguage, String translationLanguage, List<Flashcard> flashcards) {
        this.id = id;
        this.category = category;
        this.originalLanguage = originalLanguage;
        this.translationLanguage = translationLanguage;
        this.flashcards = flashcards;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTranslationLanguage() {
        return translationLanguage;
    }

    public void setTranslationLanguage(String translationLanguage) {
        this.translationLanguage = translationLanguage;
    }

    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    public void setFlashcards(List<Flashcard> flashcards) {
        this.flashcards = flashcards;
    }
}
