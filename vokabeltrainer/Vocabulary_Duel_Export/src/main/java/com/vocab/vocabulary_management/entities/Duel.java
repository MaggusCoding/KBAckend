package com.vocab.vocabulary_management.entities;

import com.management.user_management.entities.User;

import java.util.List;

public class Duel {

    private Long duelId;

    private User winner;

    private List<User> players;

    private FlashcardList flashcardList;

    public Duel(Long duelId, List<User> players, FlashcardList flashcardList) {
        this.duelId = duelId;
        this.players = players;
        this.flashcardList = flashcardList;
    }

    public Long getDuelId() {
        return duelId;
    }

    public void setDuelId(Long duelId) {
        this.duelId = duelId;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

    public List<User> getPlayers() {
        return players;
    }

    public void setPlayers(List<User> players) {
        this.players = players;
    }

    public FlashcardList getFlashcardList() {
        return flashcardList;
    }

    public void setFlashcardList(FlashcardList flashcardList) {
        this.flashcardList = flashcardList;
    }
}