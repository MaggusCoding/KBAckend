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
    @ManyToOne
    @JoinColumn(name="flashcardListId", nullable=false)
    private FlashcardList flashcardList;
    @OneToMany(mappedBy = "flashcard")
    private List<Translation> translations;
}
