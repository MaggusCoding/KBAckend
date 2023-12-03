package com.vocab.vocabulary_management.factories;

import com.vocab.vocabulary_management.entities.Flashcard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FlashcardFactory {

    @Autowired
    TranslationFactory translationFactory;

    public Flashcard.FlashcardBuilder buildDefaultFlashcardWithTranslations(){
        return Flashcard.builder().originalText("originalText").translations(List.of(translationFactory.createDefaulTranslationBuilder().build()));
    }
}
