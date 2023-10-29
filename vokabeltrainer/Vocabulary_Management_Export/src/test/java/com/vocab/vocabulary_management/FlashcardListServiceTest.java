package com.vocab.vocabulary_management;

import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management.entities.Translation;
import com.vocab.vocabulary_management.services.FlashcardListServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FlashcardListServiceTest {

    FlashcardListServiceImpl service = new FlashcardListServiceImpl();
    @Test
    void testCreateFlashcardListExpectOk(){
        FlashcardList list = new FlashcardList(1L, "English day one", "english", "deutsch",
                List.of(new Flashcard(1L, "englisches Wort", null,
                        List.of(new Translation(1L, null, "deutsches Wort")))));
        service.createFlashcardList(list);

        FlashcardList list1 = service.getById(1L);

        assertThat(list1).isNotNull();
        assertThat(list1.getCategory()).isEqualTo("English day one");
        assertThat(list1.getOriginalLanguage()).isEqualTo("english");
        assertThat(list1.getTranslationLanguage()).isEqualTo("deutsch");
        assertThat(list1.getFlashcards()).hasSize(1);

    }

    @Test
    void testGetFlashcardByFlashcardListExpectOk(){
        FlashcardList list = new FlashcardList(1L, "English day one", "english", "deutsch",
                List.of(new Flashcard(1L, "englisches Wort", null,
                        List.of(new Translation(1L, null, "deutsches Wort")))));
        service.createFlashcardList(list);

        List<Flashcard> flashcards = service.getFlashcardsByFlashcardListId(1L);

        assertThat(flashcards).isNotEmpty();
        assertThat(flashcards.get(0).getFlashCardId()).isEqualTo(1L);
        assertThat(flashcards.get(0).getOriginalText()).isEqualTo("englisches Wort");
    }

    @Test
    void testGetFlashcardByFlashcardListExpectNotFound(){
        FlashcardList list = new FlashcardList(1L, "English day one", "english", "deutsch",
                List.of(new Flashcard(1L, "englisches Wort", null,
                        List.of(new Translation(1L, null, "deutsches Wort")))));
        service.createFlashcardList(list);

        List<Flashcard> flashcards = service.getFlashcardsByFlashcardListId(2L);

        assertThat(flashcards).isEmpty();
    }

}
