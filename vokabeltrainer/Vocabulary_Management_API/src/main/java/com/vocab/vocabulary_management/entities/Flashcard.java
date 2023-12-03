package com.vocab.vocabulary_management.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Flashcard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flashCardId;
    private String originalText;
    @ManyToOne (fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE})
    @JoinColumn(name="flashcardListId", nullable=false)
    private FlashcardList flashcardList;
    @OneToMany(mappedBy = "flashcard", fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE})
    private List<Translation> translations;

    @Override
    public String toString() {
        return "Flashcard{" +
                "flashCardId=" + flashCardId +
                ", originalText='" + originalText + '\'' +
                ", flashcardListId=" + flashcardList.getFlashcardListId() +
                '}';
    }
}
