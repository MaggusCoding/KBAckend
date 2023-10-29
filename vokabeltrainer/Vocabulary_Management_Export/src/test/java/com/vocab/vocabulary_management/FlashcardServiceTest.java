package com.vocab.vocabulary_management;

import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.Translation;
import com.vocab.vocabulary_management.services.FlashcardServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FlashcardServiceTest {

    FlashcardServiceImpl service = new FlashcardServiceImpl();

    @Test
    void testCreateFlashcardExpectOk() {
        Flashcard flashcard = new Flashcard(1L, "original_text", null, List.of(new Translation(1L, null, "deutsche Übersetzung")));

        service.createFlashcard(flashcard);

        assertThat(service.getById(1L)).isNotNull();
        assertThat(service.getById(1L).getOriginalText()).isEqualTo("original_text");
        assertThat(service.getById(1L).getTranslations()).hasSize(1);
    }

    @Test
    void testCreateNullFlashcardExpectFail() {
        service.createFlashcard(null);

        assertThat(service.getAll()).isNotNull().isEmpty();
    }


}
