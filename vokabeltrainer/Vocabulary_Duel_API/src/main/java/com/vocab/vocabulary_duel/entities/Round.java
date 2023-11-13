package com.vocab.vocabulary_duel.entities;

import com.vocab.vocabulary_management.entities.Flashcard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roundId;
    @ManyToOne
    @JoinColumn(name="duelId", nullable=false)
    private Duel duel;
    @ManyToOne
    @JoinColumn(name="flashCardId", nullable=false)
    private Flashcard questionedFlashcard;
    @OneToMany(mappedBy = "round")
    private List<Answer> selectedAnswers;

}
