package com.vocab.vocabulary_management_impl;

import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management.factories.FlashcardListFactory;
import com.vocab.vocabulary_management.repos.FlashcardListRepo;
import com.vocab.vocabulary_management.repos.FlashcardRepo;
import com.vocab.vocabulary_management.repos.TranslationRepo;
import com.vocab.vocabulary_management_impl.services.FlashcardListServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FlashcardListServiceImplTest {

    private static final String testContent = "{{{holidays}}}{{{English}}}{{{Deutsch}}}{{{schreiner_4_klasse}}}" + System.lineSeparator() +
            "{Holiday} : {Urlaub}, {Ferien}" + System.lineSeparator() +
            "{to travel} : {reisen}, {unterwegs sein}" + System.lineSeparator() +
            "{Destination} : {Reiseziel}, {Zielort}, {Bestimmungsort}" + System.lineSeparator() +
            "{Beach} : {Strand},{Küste},{Ufer}" + System.lineSeparator() +
            "{Hotel} : {Hotel},{Herberge},{Gasthaus}" + System.lineSeparator() +
            "{Tourist} : {Tourist},{Besucher},{Gast}" + System.lineSeparator() +
            "{Adventure} : {Abenteuer},{Erlebnis},{Expedition}" + System.lineSeparator() +
            "{Relaxation} : {Entspannung},{Erholung},{Ruhe}" + System.lineSeparator() +
            "{Sightseeing} : {Besichtigung},{Sehenswürdigkeiten},{Stadtbesichtigung}" + System.lineSeparator() +
            "{Souvenir} : {Souvenir},{Andenken},{Erinnerungsstück}";

    private static final String additionalContent1 = "{{{english lesson one}}}{{{English}}}{{{Deutsch}}}{{{schreiner_4_klasse}}}" + System.lineSeparator() +
            "{Sunscreen} : {Sonnencreme},{Sonnenschutz},{Sonnenblocker}" + System.lineSeparator() +
            "{Cruise} : {Kreuzfahrt},{Schifffahrt},{Seereise}" + System.lineSeparator() +
            "{Explore} : {Erkunden},{Erforschen},{Entdecken}" + System.lineSeparator() +
            "{Map} : {Karte},{Landkarte},{Plan}" + System.lineSeparator() +
            "{Resort} : {Resort},{Ferienanlage},{Kurort}" + System.lineSeparator() +
            "{Culture} : {Kultur},{Zivilisation},{Gesittung}" + System.lineSeparator() +
            "{Relax} : {Entspannen},{Ausruhen},{Sich erholen}" + System.lineSeparator() +
            "{Flight} : {Flug},{Flugreise},{Flugzeugreise}" + System.lineSeparator() +
            "{originalText} : {translationText}" + System.lineSeparator() + //already exists in default flashcardlist of factory
            "{Sightseeing} : {Besichtigung},{Sehenswürdigkeiten},{Stadtbesichtigung}" + System.lineSeparator() +
            "{Train ride} : {Zugfahrt}";

    @InjectMocks
    FlashcardListServiceImpl service;

    @Autowired
    FlashcardListFactory flashcardListFactory;

    @Mock
    FlashcardListRepo flashcardListRepo;

    @Mock
    FlashcardRepo flashcardRepo;

    @Mock
    TranslationRepo translationRepo;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeleteFlashcardListExistsAndIsNotUsed() {
        FlashcardList flashcardList = flashcardListFactory.buildFlashcardListDefault().flashcardListId(1L).build();
        when(flashcardListRepo.existsById(flashcardList.getFlashcardListId())).thenReturn(true);
        when(flashcardListRepo.countDuelByFlashcardList(flashcardList.getFlashcardListId())).thenReturn(0L);

        assertThat(service.deleteFlashcardList(flashcardList.getFlashcardListId())).isTrue();
        verify(flashcardListRepo).existsById(anyLong());
        verify(flashcardListRepo).countDuelByFlashcardList(anyLong());
    }

    @Test
    void testDeleteFlashcardListNotExistsAndIsNotUsed() {
        when(flashcardListRepo.existsById(1L)).thenReturn(false);
        when(flashcardListRepo.countDuelByFlashcardList(1L)).thenReturn(0L);

        assertThat(service.deleteFlashcardList(1L)).isFalse();
        verify(flashcardListRepo).existsById(anyLong());
        verify(flashcardListRepo, times(0)).countDuelByFlashcardList(anyLong());
    }

    @Test
    void testDeleteFlashcardListAttachedToDuel() {
        FlashcardList flashcardList = flashcardListFactory.buildFlashcardListDefault().flashcardListId(1L).build();
        when(flashcardListRepo.existsById(flashcardList.getFlashcardListId())).thenReturn(true);
        when(flashcardListRepo.countDuelByFlashcardList(flashcardList.getFlashcardListId())).thenReturn(1L);

        assertThat(service.deleteFlashcardList(flashcardList.getFlashcardListId())).isFalse();
        verify(flashcardListRepo).countDuelByFlashcardList(anyLong());
        verify(flashcardListRepo).existsById(anyLong());
    }

    @Test
    void testCreateFlashcardListExpectOk() {
        when(flashcardListRepo.findByCategory(any())).thenReturn(null);
        when(flashcardRepo.existsByOriginalText(any())).thenReturn(false);

        boolean result = service.createFlashcardList(testContent);

        assertThat(result).isTrue();
        verify(flashcardListRepo).findByCategory(any());
        verify(flashcardListRepo).save(any());
        verify(flashcardRepo, times(10)).existsByOriginalText(any());
        verify(flashcardRepo, times(10)).save(any());
        verify(translationRepo, times(28)).save(any());

    }

    @Test
    void testCreateFlashcardListAddingAdditionalVocabularyToExistingFlashcardList() {
        FlashcardList initialFlashcardList = flashcardListFactory.buildFlashcardListDefault().build();
        when(flashcardListRepo.findByCategory(any())).thenReturn(initialFlashcardList);
        when(flashcardRepo.existsByOriginalText("originalText")).thenReturn(true);

        boolean result = service.createFlashcardList(additionalContent1);

        assertThat(result).isTrue();
        verify(flashcardListRepo).findByCategory(any());
        verify(flashcardListRepo, times(0)).save(any());
        verify(flashcardRepo, times(11)).existsByOriginalText(any());
        verify(flashcardRepo, times(10)).save(any());
        verify(translationRepo, times(28)).save(any());

    }

    @Test
    void testGetAllFlashcardListExpect2() {
        FlashcardList flashcardList1 = flashcardListFactory.buildFlashcardListDefault().build();
        FlashcardList flashcardList2 = flashcardListFactory.buildFlashcardListDefault().category("time").build();
        when(flashcardListRepo.findAll()).thenReturn(List.of(flashcardList1, flashcardList2));

        List<FlashcardList> flashcardLists = service.getAll();

        assertThat(flashcardLists).isNotNull().hasSize(2);
        assertThat(flashcardLists).extracting("category").contains("english lesson one", "time");
        verify(flashcardListRepo).findAll();
    }

    @Test
    void testGetFlashcardByIdExpectOk() {
        FlashcardList expectedFlashcardList = flashcardListFactory.buildFlashcardListDefault().build();
        when(flashcardListRepo.findById(expectedFlashcardList.getFlashcardListId())).thenReturn(Optional.of(expectedFlashcardList));

        FlashcardList flashcardList = service.getById(expectedFlashcardList.getFlashcardListId());

        assertThat(flashcardList).isNotNull();
        assertThat(flashcardList.getFlashcardListId()).isEqualTo(expectedFlashcardList.getFlashcardListId());
        verify(flashcardListRepo).findById(expectedFlashcardList.getFlashcardListId());
    }

    @Test
    void testGetFlashcardByIdExpectNoResult() {
        FlashcardList expectedFlashcardList = flashcardListFactory.buildFlashcardListDefault().build();
        when(flashcardListRepo.findById(expectedFlashcardList.getFlashcardListId())).thenReturn(Optional.empty());

        FlashcardList flashcardList = service.getById(expectedFlashcardList.getFlashcardListId());

        assertThat(flashcardList).isNull();
        verify(flashcardListRepo).findById(expectedFlashcardList.getFlashcardListId());
    }

}
