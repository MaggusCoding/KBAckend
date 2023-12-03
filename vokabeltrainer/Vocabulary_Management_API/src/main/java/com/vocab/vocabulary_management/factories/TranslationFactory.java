package com.vocab.vocabulary_management.factories;

import com.vocab.vocabulary_management.entities.Translation;
import org.springframework.stereotype.Component;

@Component
public class TranslationFactory {

    public Translation.TranslationBuilder createDefaulTranslationBuilder(){
        return Translation.builder().translationText("translationText");
    }
}
