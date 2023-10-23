package com.vocab.vocabulary_management.entities;

import java.util.List;

public class Round {

    private long roundId;
    private Duel duel;
    private Flashcard questionedFlashcard;
    private List<Answer> answers;
    private List<String> wrongAnswers;

    public Round(long roundId, Duel duel, Flashcard questionedFlashcard, List<Answer> answers, List<String> wrongAnswers) {
        this.roundId = roundId;
        this.duel = duel;
        this.questionedFlashcard = questionedFlashcard;
        this.answers = answers;
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

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<String> getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(List<String> wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }
}
