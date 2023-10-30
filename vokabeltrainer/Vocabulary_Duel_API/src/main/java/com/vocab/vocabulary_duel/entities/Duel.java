package com.vocab.vocabulary_duel.entities;

import com.management.user_management.entities.User;
import com.vocab.vocabulary_management.entities.Flashcard;

import java.util.List;

public class Duel {

    private Long duelId;

    private List<User> winner;

    private List<User> players;

    private List<Flashcard> flashcardsForDuel;

    public Duel(Long duelId, List<User> players, List<Flashcard> flashcardsForDuel) {
        this.duelId = duelId;
        this.players = players;
        this.flashcardsForDuel = flashcardsForDuel;
    }

    public Long getDuelId() {
        return duelId;
    }

    public void setDuelId(Long duelId) {
        this.duelId = duelId;
    }

    public List<User> getWinner() {
        return winner;
    }

    public void setWinner(List<User> winner) {
        this.winner = winner;
    }

    public List<User> getPlayers() {
        return players;
    }

    public void setPlayers(List<User> players) {
        this.players = players;
    }

    public List<Flashcard> getFlashcardsForDuel() {
        return flashcardsForDuel;
    }

    public void setFlashcardsForDuel(List<Flashcard> flashcardsForDuel) {
        this.flashcardsForDuel = flashcardsForDuel;
    }

}