package com.vocab.vocabulary_management.entities;

public class Translation {

    private Long id;

    private String originalText;

    private String translation;

    public Translation(Long id, String originalText, String translation) {
        this.id = id;
        this.originalText = originalText;
        this.translation = translation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
