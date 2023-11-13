package com.vocab.vocabulary_duel.entities;

import com.management.user_management.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long answerId;
    @ManyToOne
    @JoinColumn(name="userId", nullable=false)
    private UserEntity player;
    @ManyToOne
    @JoinColumn(name="roundId", nullable=false)
    private Round round;
    private Boolean correct;


}
