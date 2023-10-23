package com.vocab.vocabulary_management.entities;

import com.management.user_management.entities.User;

public class Duel {

    private Long duelId;

    private User player1;

    private User player2;

    private User winner;

    private Integer score1;

    private Integer score2;

    private FlashcardList flashcardList;
}