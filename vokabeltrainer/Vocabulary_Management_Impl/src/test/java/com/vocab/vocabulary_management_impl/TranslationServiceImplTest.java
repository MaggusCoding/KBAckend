package com.vocab.vocabulary_management_impl;

import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.Translation;
import com.vocab.vocabulary_management_impl.services.TranslationServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TranslationServiceImplTest {

    TranslationServiceImpl service = new TranslationServiceImpl();
    @Test
    void testCreateTranslationExpectOk() {
        Translation translation = new Translation(1L, new Flashcard(1L,"think", null, null), "sink");
        service.createTranslation(translation);

        Translation translation1 = service.getById(1L);
        assertThat(translation1).isNotNull();
        assertThat(translation1.getTranslationText()).isEqualTo("sink");
        assertThat(translation1.getFlashcard()).isNotNull();
        assertThat(translation1.getFlashcard().getOriginalText()).isEqualTo("think");

    }

    @Test
    void testGetTranslationsByFlashcardExpectOk(){
        Translation translation = new Translation(1L, new Flashcard(1L,"think", null, null), "sink");
        Translation translation2 = new Translation(2L, new Flashcard(1L,"think", null, null), "tink");
        service.createTranslation(translation);
        service.createTranslation(translation2);

        List<Translation> translations = service.getAllTranslationByFlashcard(1L);

        assertThat(translations).isNotEmpty().hasSize(2);
        assertThat(translations.get(0).getTranslationText()).isEqualTo("sink");
        assertThat(translations.get(1).getTranslationText()).isEqualTo("tink");
    }

}
