package com.vocab.vocabulary_duel_API.dto;

import lombok.Getter;

@Getter
public class RankingPlayer {
    private Long playerId;

    private Long amountCorrectAnswer;

    public RankingPlayer(Long playerId, Long amountCorrectAnswer){
        this.playerId = playerId;
        this.amountCorrectAnswer = amountCorrectAnswer;
    }

}
