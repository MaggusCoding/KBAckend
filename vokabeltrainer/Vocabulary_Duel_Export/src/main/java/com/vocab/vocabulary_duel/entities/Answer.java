package com.vocab.vocabulary_duel.entities;

import com.management.user_management.entities.User;
import com.vocab.vocabulary_management.entities.Flashcard;

public class Answer {

    private long answerId;
    private User player;
    private Flashcard flashcard;
    private Round round;
    private Boolean correctAnswer;

    public Answer(long answerId, User player, Flashcard flashcard, Round round, Boolean correctAnswer) {
        this.answerId = answerId;
        this.player = player;
        this.flashcard = flashcard;
        this.round = round;
        this.correctAnswer = correctAnswer;
    }

    public long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(long answerId) {
        this.answerId = answerId;
    }

    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    public Flashcard getFlashcard() {
        return flashcard;
    }

    public void setFlashcard(Flashcard flashcard) {
        this.flashcard = flashcard;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public Boolean getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
