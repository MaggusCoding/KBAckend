package com.vocab.vocabulary_duel.entities;

import com.management.user_management.entities.User;
import com.vocab.vocabulary_management.entities.Flashcard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "answer")
@Data
@NoArgsConstructor
public class Answer {
    @Id
    private long answerId;
    private User player;
    private Flashcard flashcard;
    private Round round;
    private Boolean correct;

    public Answer(long answerId, User player, Flashcard flashcard, Round round, String selectedAnswer) {
        this.answerId = answerId;
        this.player = player;
        this.flashcard = flashcard;
        this.round = round;
    }
}
