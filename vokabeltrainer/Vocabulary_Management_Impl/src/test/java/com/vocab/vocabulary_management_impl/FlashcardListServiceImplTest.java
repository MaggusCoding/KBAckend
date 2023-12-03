package com.vocab.vocabulary_management_impl;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management.repos.UserRepo;
import com.vocab.user_management_impl.factories.UserFactory;
import com.vocab.vocabulary_duel.entities.Duel;
import com.vocab.vocabulary_duel.repositories.DuelRepo;
import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management.factories.FlashcardListFactory;
import com.vocab.vocabulary_management.repos.FlashcardListRepo;
import com.vocab.vocabulary_management_impl.services.FlashcardListServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FlashcardListServiceImplTest {

    private static final String testContent = "{{{holidays}}}{{{English}}}{{{Deutsch}}}{{{schreiner_4_klasse}}}" + System.lineSeparator() +
            "{Holiday} : {Urlaub}, {Ferien}" + System.lineSeparator()+
            "{to travel} : {reisen}, {unterwegs sein}" + System.lineSeparator() +
            "{Destination} : {Reiseziel}, {Zielort}, {Bestimmungsort}" + System.lineSeparator() +
            "{Beach} : {Strand},{Küste},{Ufer}" + System.lineSeparator()+
            "{Hotel} : {Hotel},{Herberge},{Gasthaus}" + System.lineSeparator()+
            "{Tourist} : {Tourist},{Besucher},{Gast}" + System.lineSeparator()+
            "{Adventure} : {Abenteuer},{Erlebnis},{Expedition}" + System.lineSeparator()+
            "{Relaxation} : {Entspannung},{Erholung},{Ruhe}" + System.lineSeparator()+
            "{Sightseeing} : {Besichtigung},{Sehenswürdigkeiten},{Stadtbesichtigung}" + System.lineSeparator()+
            "{Souvenir} : {Souvenir},{Andenken},{Erinnerungsstück}";

    private static final String testContent2 = "{{{time}}}{{{English}}}{{{Deutsch}}}{{{schreiner_4_klasse}}}" + System.lineSeparator() +
            "{afternoon} : {Nachmittag}" + System.lineSeparator() +
            "{clock} : {Uhr}" + System.lineSeparator() +
            "{eleven} : {elf}" + System.lineSeparator() +
            "{fifty} : {fünfzehn}" + System.lineSeparator() +
            "{Friday} : {Freitag}" + System.lineSeparator() +
            "{half} : {halb}" + System.lineSeparator() +
            "{midnight} : {Mitternacht}" + System.lineSeparator() +
            "{Monday} : {Montag}" + System.lineSeparator() +
            "{night} : {Nacht}" + System.lineSeparator() +
            "{past} : {nach}";

    private static final String additionalContent1 = "{{{holidays}}}{{{English}}}{{{Deutsch}}}{{{schreiner_4_klasse}}}" + System.lineSeparator() +
            "{Sunscreen} : {Sonnencreme},{Sonnenschutz},{Sonnenblocker}" + System.lineSeparator() +
            "{Cruise} : {Kreuzfahrt},{Schifffahrt},{Seereise}" + System.lineSeparator() +
            "{Explore} : {Erkunden},{Erforschen},{Entdecken}" + System.lineSeparator() +
            "{Map} : {Karte},{Landkarte},{Plan}" + System.lineSeparator() +
            "{Resort} : {Resort},{Ferienanlage},{Kurort}" + System.lineSeparator() +
            "{Culture} : {Kultur},{Zivilisation},{Gesittung}" + System.lineSeparator() +
            "{Relax} : {Entspannen},{Ausruhen},{Sich erholen}" + System.lineSeparator() +
            "{Flight} : {Flug},{Flugreise},{Flugzeugreise}" + System.lineSeparator() +
            "{Suitcase} : {Koffer},{Reisekoffer},{Gepäckstück}" + System.lineSeparator() +
            "{Sightseeing} : {Besichtigung},{Sehenswürdigkeiten},{Stadtbesichtigung}" + System.lineSeparator() +
            "{Train ride} : {Zugfahrt}";

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

    @BeforeEach
    void clearDb(){
        duelRepo.deleteAll();
        flashcardListRepo.deleteAll();
    }

    @Test
    void testDeleteFlashcardList(){
        FlashcardList flashcardList = flashcardListFactory.buildFlashcardListDefault();
        flashcardListRepo.save(flashcardList);

        assertThat(service.deleteFlashcardList(flashcardList.getFlashcardListId())).isTrue();
    }

    @Test
    void testDeleteFlashcardListAttachedToDuel(){
        FlashcardList flashcardList = flashcardListRepo.save(flashcardListFactory.buildFlashcardListDefault());
        List<UserEntity> users = userFactory.createUserListSize2();
        userRepo.saveAll(users);
        duelRepo.save(Duel.builder().flashcardsForDuel(flashcardList).players(users).build());

        assertThat(service.deleteFlashcardList(flashcardList.getFlashcardListId())).isFalse();
    }

    // TODO: Add other tests
    @Test
    void testCreateFlashcardListExpectOk(){
        service.createFlashcardList(testContent);

        FlashcardList list1 = flashcardListRepo.findByCategory("holidays");

        assertThat(list1).isNotNull();
        assertThat(list1.getCategory()).isEqualTo("holidays");
        assertThat(list1.getOriginalLanguage()).isEqualTo("English");
        assertThat(list1.getTranslationLanguage()).isEqualTo("Deutsch");
        assertThat(list1.getFlashcards()).hasSize(10);

    }

    @Test
    void testGetFlashcardByFlashcardListExpectOk(){
        service.createFlashcardList(testContent);
        long flashcardListId = flashcardListRepo.findByCategory("holidays").getFlashcardListId();

        List<Flashcard> flashcards = service.getFlashcardsByFlashcardListId(flashcardListId);

        assertThat(flashcards).isNotNull().hasSize(10);
        assertThat(flashcards.get(0).getOriginalText()).isEqualTo("Holiday");
        assertThat(flashcards.get(0).getTranslations()).isNotNull().hasSize(2);
    }

    @Test
    void testGetAllFlashcardListExpect2(){
        service.createFlashcardList(testContent);
        service.createFlashcardList(testContent2);

        List<FlashcardList> flashcardLists = service.getAll();

        assertThat(flashcardLists).isNotNull().hasSize(2);
        assertThat(flashcardLists).extracting("category").contains("holidays","time");

    }

    @Test
    void testCreateFlashcardListAddingAdditionalVocabularyToExistingFlashcardList(){
        service.createFlashcardList(testContent);

        List<FlashcardList> flashcardListsBefore = service.getAll();
        assertThat(flashcardListsBefore).isNotNull().hasSize(1);
        List<Flashcard> flashcardList = flashcardListsBefore.get(0).getFlashcards();
        assertThat(flashcardListsBefore.get(0).getFlashcards()).isNotNull().hasSize(10);

        service.createFlashcardList(additionalContent1);

        List<FlashcardList> flashcardListsAfter = service.getAll();
        assertThat(flashcardListsAfter).isNotNull().hasSize(1);
        assertThat(flashcardListsAfter.get(0).getCategory()).isEqualTo("holidays");
        assertThat(flashcardListsAfter.get(0).getFlashcards()).isNotNull().hasSize(20);

    }

}
