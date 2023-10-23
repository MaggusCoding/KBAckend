package com.vocab.vocabulary_management.entities;

public class Round {

    private long roundId;
    private Duel duel;
    private Flashcard questionedFlashcard;
    private boolean user1Correct;
    private boolean user2Correct;
    private String[] wrongAnswers;
}
