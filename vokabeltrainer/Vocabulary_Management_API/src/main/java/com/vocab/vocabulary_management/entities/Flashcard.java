package com.vocab.vocabulary_management.entities;

import java.util.List;

public class Flashcard {
    private Long flashCardId;
    private String originalText;
    private FlashcardList flashcardList;
    private List<Translation> translations;

    public Flashcard(Long flashCardId, String originalText, FlashcardList flashcardList, List<Translation> translations) {
        this.flashCardId = flashCardId;
        this.originalText = originalText;
        this.flashcardList = flashcardList;
        this.translations = translations;
    }

    public Long getFlashCardId() {
        return flashCardId;
    }

    public void setFlashCardId(Long flashCardId) {
        this.flashCardId = flashCardId;
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public FlashcardList getFlashcardList() {
        return flashcardList;
    }

    public void setFlashcardList(FlashcardList flashcardList) {
        this.flashcardList = flashcardList;
    }

    public List<Translation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Translation> translations) {
        this.translations = translations;
    }
}
