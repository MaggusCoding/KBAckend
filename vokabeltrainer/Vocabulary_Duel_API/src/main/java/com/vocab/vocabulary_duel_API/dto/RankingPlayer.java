package com.vocab.vocabulary_duel_API.dto;

import lombok.Getter;

@Getter
public class RankingPlayer {
    private String player;

    private Long amountCorrectAnswer;

    public RankingPlayer(String player, Long amountCorrectAnswer){
        this.player = player;
        this.amountCorrectAnswer = amountCorrectAnswer;
    }

}
