package com.vocab.vocabulary_management_impl;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management.repos.UserRepo;
import com.vocab.user_management_impl.factories.UserFactory;
import com.vocab.vocabulary_duel.entities.Duel;
import com.vocab.vocabulary_duel.repositories.DuelRepo;
import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management.factories.FlashcardListFactory;
import com.vocab.vocabulary_management.repos.FlashcardListRepo;
import com.vocab.vocabulary_management_impl.services.FlashcardListServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FlashcardListServiceImplTest_old {

    @Autowired
    FlashcardListServiceImpl service;

    @Autowired
    FlashcardListFactory flashcardListFactory;

    @Autowired
    UserFactory userFactory;

    @Autowired
    UserRepo userRepo;

    @Autowired
    FlashcardListRepo flashcardListRepo;

    @Autowired
    DuelRepo duelRepo;

    @Test
    void testDeleteFlashcardList(){
        flashcardListRepo.save(flashcardListFactory.createFlashcardListDefault());
        System.out.println("count flashcardlists before " + service.getAll().size());

        assertThat(service.deleteFlashcardList(1L)).isTrue();
        assertThat(service.getAll()).hasSize(0);
    }

    @Test
    void testDeleteFlashcardListAttachedToDuel(){
        FlashcardList flashcardList = flashcardListRepo.save(flashcardListFactory.createFlashcardListDefault());
        List<UserEntity> users = userFactory.createUserListSize2();
        userRepo.save(users.get(0));
        userRepo.save(users.get(1));
        duelRepo.save(Duel.builder().flashcardsForDuel(flashcardList).players(users).build());
        assertThat(service.getAll()).hasSize(1);
        assertThat(service.deleteFlashcardList(1L)).isFalse();
    }

//    @Test
//    void testCreateFlashcardListExpectOk(){
//        service.createFlashcardList("Unit 4 My trip to Ireland - Part A.txt");
//
//        FlashcardList list1 = service.getById(1L);
//
//        assertThat(list1).isNotNull();
//        assertThat(list1.getCategory()).isEqualTo("English day one");
//        assertThat(list1.getOriginalLanguage()).isEqualTo("bank");
//        assertThat(list1.getTranslationLanguage()).isEqualTo("Bank");
//        assertThat(list1.getFlashcards()).hasSize(1);
//
//    }
//
//    @Test
//    void testGetFlashcardByFlashcardListExpectOk(){
//        service.createFlashcardList("Unit 4 My trip to Ireland - Part A.txt");
//
//        List<Flashcard> flashcards = service.getFlashcardsByFlashcardListId(1L);
//
//        assertThat(flashcards).isNotEmpty();
//        assertThat(flashcards.get(0).getFlashCardId()).isEqualTo(1L);
//        assertThat(flashcards.get(0).getOriginalText()).isEqualTo("(to) be left");
//    }
//
//    @Test
//    void testCreateFlashcardListExpectFileNotFoundException(){
//
//        assertThatThrownBy(() -> service.createFlashcardList("Unit 1 This is London - Part XYZ.txt")).isInstanceOf(FileNotFoundException.class);
//
//    }
//
//    @Test
//    void testCreateFlashcardListExpectMalformedException(){
//
//        assertThatThrownBy(() -> service.createFlashcardList("Unit 1 This is London - Part Malformed.txt")).isInstanceOf(MalformedInputException.class);
//
//    }
//
//    @Test
//    void testCreateFlashcardListExpectInvalidFormatException(){
//        assertThatThrownBy(() -> service.createFlashcardList("Unit 1 This is London - Part A.txt")).isInstanceOf(InvalidFormatException.class);
//    }
//
//    @Test
//    void testGetAllFlashcardListExpect2(){
//        service.createFlashcardList("Unit 4 My trip to Ireland - Part A.txt");
//        service.createFlashcardList("Unit 1 Block A.txt");
//
//        List<FlashcardList> flashcardLists = service.getAll();
//
//        assertThat(flashcardLists).isNotNull().hasSize(2);
//        assertThat(flashcardLists).extracting("category").contains("Unit 4 My trip to Ireland - Part A.txt","Unit 1 Block A.txt");
//
//    }

}
