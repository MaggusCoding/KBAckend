package com.vocab.vocabulary_management.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Flashcard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flashCardId;
    private String originalText;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name="flashcardListId", nullable=false)
    private FlashcardList flashcardList;
    @OneToMany(mappedBy = "flashcard", fetch = FetchType.EAGER)
    private List<Translation> translations;
}
