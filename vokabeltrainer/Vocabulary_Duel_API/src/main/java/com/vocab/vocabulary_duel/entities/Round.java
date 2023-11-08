package com.vocab.vocabulary_duel.entities;

import com.management.user_management.entities.User;
import com.vocab.vocabulary_management.entities.Flashcard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Round {
    @Id
    private long roundId;
    private Duel duel;
    private Flashcard questionedFlashcard;
    private List<Answer> selectedAnswers;
    private List<String> wrongAnswers;
}
