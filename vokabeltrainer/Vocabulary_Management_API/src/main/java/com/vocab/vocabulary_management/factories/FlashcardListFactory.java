package com.vocab.vocabulary_management.factories;

import com.vocab.vocabulary_management.entities.FlashcardList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FlashcardListFactory {

    @Autowired
    FlashcardFactory flashcardFactory;

    public FlashcardList buildFlashcardListDefault(){
        return FlashcardList.builder().category("english lesson one").flashcards(List.of(flashcardFactory.buildDefaultFlashcardWithTranslations().build())).build();
    }

    public FlashcardList buildDefaultFlashcardListWithoutFlashcards() {
        return FlashcardList.builder().category("english lesson one").translationLanguage("German").originalLanguage("English").build();
    }
}
