package com.vocab.vocabulary_management.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "flashcard_list")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class FlashcardList {
    @Id
    private Long flashcardListId;
    private String category;

    private String originalLanguage;

    private String translationLanguage;

    private List<Flashcard> flashcards;

}