package com.vocab.vocabulary_management.entities;

import java.util.List;

public class Flashcard {
    private Long flashCardId;
    private String originalText;
    private Translation translation;
    private List<Synonym> synonyms;

    public Flashcard(Long flashCardId, String originalText, Translation translation, List<Synonym> synonyms) {
        this.flashCardId = flashCardId;
        this.originalText = originalText;
        this.translation = translation;
        this.synonyms = synonyms;
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

    public Translation getTranslation() {
        return translation;
    }

    public void setTranslation(Translation translation) {
        this.translation = translation;
    }

    public List<Synonym> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<Synonym> synonyms) {
        this.synonyms = synonyms;
    }
}
