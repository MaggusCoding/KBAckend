package com.vocab.vocabulary_management.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "flashcard_list")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlashcardList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flashcardListId;

    private String category;

    private String originalLanguage;

    private String translationLanguage;

    @OneToMany(mappedBy = "flashcardList", fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE})
    private List<Flashcard> flashcards;

    //make javadoc happy
    public static class FlashcardListBuilder{}

    @Override
    public String toString() {
        return "FlashcardList{" +
                "flashcardListId=" + flashcardListId +
                ", category='" + category + '\'' +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", translationLanguage='" + translationLanguage + '\'' +
                '}';
    }
}