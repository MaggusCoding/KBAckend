package com.vocab.vocabulary_management.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;
@Entity
@Table(name = "flashcard")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Flashcard {
    @Id
    private Long flashCardId;
    private String originalText;
    private FlashcardList flashcardList;
    private List<Translation> translations;

}
