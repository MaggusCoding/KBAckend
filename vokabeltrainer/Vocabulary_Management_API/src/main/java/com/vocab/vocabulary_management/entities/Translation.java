package com.vocab.vocabulary_management.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "translation")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Translation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long translationId;

    @ManyToOne (fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE})
    @JoinColumn(name="flashCardId", nullable=false)
    private Flashcard flashcard;

    private String translationText;

    @Override
    public String toString() {
        return "Translation{" +
                "translationId=" + translationId +
                ", flashcardId=" + flashcard.getFlashCardId() +
                ", translationText='" + translationText + '\'' +
                '}';
    }

}
