package com.vocab.vocabulary_duel_API.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name="duelId", nullable=false)
    private Duel duel;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name="flashCardId", nullable=false)
    private Flashcard questionedFlashcard;
    @OneToMany(mappedBy = "round", fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE})
    private List<Answer> selectedAnswers;
    private String wrongAnswers;
    private boolean activeRound = false;

    @Version
    private Long changeCounter;

    @Override
    public String toString() {
        return "Round{" +
                "roundId=" + roundId +
                ", questionedFlashcard=" + (questionedFlashcard != null ? questionedFlashcard.getOriginalText() : null) +
                ", wrongAnswers='" + wrongAnswers + '\'' +
                ", activeRound=" + activeRound +
                '}';
    }
    public void setSingleAnswer(Answer answer){
        this.selectedAnswers.add(answer);
    }
}
