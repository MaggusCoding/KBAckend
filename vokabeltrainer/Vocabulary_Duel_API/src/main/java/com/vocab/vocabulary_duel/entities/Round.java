package com.vocab.vocabulary_duel.entities;

import com.vocab.vocabulary_management.entities.Flashcard;

import java.util.List;

public class Round {

    private long roundId;
    private Duel duel;
    private Flashcard questionedFlashcard;
    private List<Answer> selectedAnswers;
    private List<String> wrongAnswers;

    public Round(long roundId, Duel duel, Flashcard questionedFlashcard, List<Answer> selectedAnswers, List<String> wrongAnswers) {
        this.roundId = roundId;
        this.duel = duel;
        this.questionedFlashcard = questionedFlashcard;
        this.selectedAnswers = selectedAnswers;
        this.wrongAnswers = wrongAnswers;
    }

    public long getRoundId() {
        return roundId;
    }

    public void setRoundId(long roundId) {
        this.roundId = roundId;
    }

    public Duel getDuel() {
        return duel;
    }

    public void setDuel(Duel duel) {
        this.duel = duel;
    }

    public Flashcard getQuestionedFlashcard() {
        return questionedFlashcard;
    }

    public void setQuestionedFlashcard(Flashcard questionedFlashcard) {
        this.questionedFlashcard = questionedFlashcard;
    }

    public List<Answer> getSelectedAnswers() {
        return selectedAnswers;
    }

    public void setSelectedAnswers(List<Answer> selectedAnswers) {
        this.selectedAnswers = selectedAnswers;
    }

    public List<String> getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(List<String> wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }
}
