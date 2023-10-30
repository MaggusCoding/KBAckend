package com.vocab.vocabulary_management.entities;

import java.util.List;

public class FlashcardList {

    private Long flashcardListId;
    private String category;

    private String originalLanguage;

    private String translationLanguage;

    private List<Flashcard> flashcards;

    public FlashcardList(Long flashcardListId, String category, String originalLanguage, String translationLanguage, List<Flashcard> flashcards) {
        this.flashcardListId = flashcardListId;
        this.category = category;
        this.originalLanguage = originalLanguage;
        this.translationLanguage = translationLanguage;
        this.flashcards = flashcards;
    }

    public Long getFlashcardListId() {
        return flashcardListId;
    }

    public void setFlashcardListId(Long flashcardListId) {
        this.flashcardListId = flashcardListId;
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