package com.vocab.vocabulary_management.entities;

import com.vocab.vocabulary_management.entities.Flashcard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "translation")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Translation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long translationId;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name="flashCardId", nullable=false)
    private Flashcard flashcard;

    private String translationText;

}
