package com.vocab.vocabulary_management.factories;

import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management.entities.Translation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FlashcardListFactory {

    public FlashcardList createFlashcardListDefault(){
        return FlashcardList.builder().flashcards(List.of(Flashcard.builder().translations(List.of(Translation.builder().translationText("translationText").build())).originalText("originalText").build())).build();
    }
}
