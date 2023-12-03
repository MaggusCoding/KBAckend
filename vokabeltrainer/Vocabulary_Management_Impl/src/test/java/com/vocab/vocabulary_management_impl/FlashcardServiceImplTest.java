package com.vocab.vocabulary_management_impl;

import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.Translation;
import com.vocab.vocabulary_management_impl.services.FlashcardServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// TODO: Überprüfen, ob noch gebraucht
public class FlashcardServiceImplTest {

    FlashcardServiceImpl service = new FlashcardServiceImpl();

    @Test
    void testCreateFlashcardExpectOk() {
        Flashcard flashcard = new Flashcard(1L, "original_text", null, List.of(new Translation(1L, null, "deutsche Übersetzung")));

        service.createFlashcard(flashcard);

        assertThat(service.getById(1L)).isNotNull();
        assertThat(service.getById(1L).getOriginalText()).isEqualTo("original_text");
        assertThat(service.getById(1L).getTranslations()).hasSize(1);
    }

}
