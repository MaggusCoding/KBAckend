package com.vocab.vocabulary_management.entities;

public class Translation {

    private Long translationId;

    private Flashcard flashcard;

    private String translationText;

    public Translation(Long translationId, Flashcard flashcard, String translationText) {
        this.translationId = translationId;
        this.flashcard = flashcard;
        this.translationText = translationText;
    }

    public Long getTranslationId() {
        return translationId;
    }

    public void setTranslationId(Long translationId) {
        this.translationId = translationId;
    }

    public Flashcard getFlashcard() {
        return flashcard;
    }

    public void setFlashcard(Flashcard flashcard) {
        this.flashcard = flashcard;
    }

    public String getTranslationText() {
        return translationText;
    }

    public void setTranslationText(String translationText) {
        this.translationText = translationText;
    }
}
