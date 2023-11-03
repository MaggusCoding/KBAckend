package com.vocab.vocabulary_management_impl;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management_impl.services.FlashcardListServiceImpl;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.nio.charset.MalformedInputException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FlashcardListServiceImplTest {

    FlashcardListServiceImpl service = new FlashcardListServiceImpl();
    @Test
    void testCreateFlashcardListExpectOk(){
        service.createFlashcardList("Unit 4 My trip to Ireland - Part A.txt");

        FlashcardList list1 = service.getById(1L);

        assertThat(list1).isNotNull();
        assertThat(list1.getCategory()).isEqualTo("English day one");
        assertThat(list1.getOriginalLanguage()).isEqualTo("bank");
        assertThat(list1.getTranslationLanguage()).isEqualTo("Bank");
        assertThat(list1.getFlashcards()).hasSize(1);

    }

    @Test
    void testGetFlashcardByFlashcardListExpectOk(){
        service.createFlashcardList("Unit 4 My trip to Ireland - Part A.txt");

        List<Flashcard> flashcards = service.getFlashcardsByFlashcardListId(1L);

        assertThat(flashcards).isNotEmpty();
        assertThat(flashcards.get(0).getFlashCardId()).isEqualTo(1L);
        assertThat(flashcards.get(0).getOriginalText()).isEqualTo("(to) be left");
    }

    @Test
    void testCreateFlashcardListExpectFileNotFoundException(){

        assertThatThrownBy(() -> service.createFlashcardList("Unit 1 This is London - Part XYZ.txt")).isInstanceOf(FileNotFoundException.class);

    }

    @Test
    void testCreateFlashcardListExpectMalformedException(){

        assertThatThrownBy(() -> service.createFlashcardList("Unit 1 This is London - Part Malformed.txt")).isInstanceOf(MalformedInputException.class);

    }

    @Test
    void testCreateFlashcardListExpectInvalidFormatException(){
        assertThatThrownBy(() -> service.createFlashcardList("Unit 1 This is London - Part A.txt")).isInstanceOf(InvalidFormatException.class);
    }

    @Test
    void testGetAllFlashcardListExpect2(){
        service.createFlashcardList("Unit 4 My trip to Ireland - Part A.txt");
        service.createFlashcardList("Unit 1 Block A.txt");

        List<FlashcardList> flashcardLists = service.getAll();

        assertThat(flashcardLists).isNotNull().hasSize(2);
        assertThat(flashcardLists).extracting("category").contains("Unit 4 My trip to Ireland - Part A.txt","Unit 1 Block A.txt");

    }

}
