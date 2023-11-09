package com.vocab.vocabulary_duel.entities;

import com.management.user_management.entities.User;
import com.vocab.vocabulary_management.entities.Flashcard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "answer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    @Id
    private long answerId;
    @ManyToOne
    @JoinColumn(name="playerId", nullable=false)
    private User player;
    @ManyToOne
    @JoinColumn(name="roundId", nullable=false)
    private Round round;
    private Boolean correct;


}
