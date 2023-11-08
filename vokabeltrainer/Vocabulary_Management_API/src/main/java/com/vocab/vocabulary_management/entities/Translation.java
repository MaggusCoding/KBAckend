package com.vocab.vocabulary_management.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "translation")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Translation {
    @Id
    private Long translationId;

    private Flashcard flashcard;

    private String translationText;

}
