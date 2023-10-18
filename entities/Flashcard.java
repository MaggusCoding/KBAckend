package com.example.vocabulary_management.entities;

import java.util.List;

public class Flashcard {
    private int flashCardId;
    private String originalText;
    private String translation;
    private String originalLanguage;
    private String translationLanguage;
    private List<Category> categories;

    public Flashcard(int flashCardId, String originalText, String translation, String originalLanguage, String translationLanguage, List<Category> categories) {
        this.flashCardId = flashCardId;
        this.originalText = originalText;
        this.translation = translation;
        this.originalLanguage = originalLanguage;
        this.translationLanguage = translationLanguage;
        this.categories = categories;
    }

    public int getFlashCardId() {
        return flashCardId;
    }

    public void setFlashCardId(int flashCardId) {
        this.flashCardId = flashCardId;
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
