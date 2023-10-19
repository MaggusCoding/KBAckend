package com.vocab.vocabulary_management.entities;

import com.management.user_management.entities.User;

public class Duel {

    private Long duelId;

    private User player1;

    private User player2;

    private User winner;

    private Integer score1;

    private Integer score2;

    public Duel(Long duelId, User player1, User player2, User winner, Integer score1, Integer score2) {
        this.duelId = duelId;
        this.player1 = player1;
        this.player2 = player2;
        this.winner = winner;
        this.score1 = score1;
        this.score2 = score2;
    }

    public Long getDuelId() {
        return duelId;
    }

    public void setDuelId(Long duelId) {
        this.duelId = duelId;
    }

    public User getPlayer1() {
        return player1;
    }

    public void setPlayer1(User player1) {
        this.player1 = player1;
    }

    public User getPlayer2() {
        return player2;
    }

    public void setPlayer2(User player2) {
        this.player2 = player2;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

    public Integer getScore1() {
        return score1;
    }

    public void setScore1(Integer score1) {
        this.score1 = score1;
    }

    public Integer getScore2() {
        return score2;
    }

    public void setScore2(Integer score2) {
        this.score2 = score2;
    }
}
