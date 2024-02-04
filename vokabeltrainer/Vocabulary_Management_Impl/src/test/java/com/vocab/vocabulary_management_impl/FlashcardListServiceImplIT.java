package com.vocab.vocabulary_management_impl;

import com.vocab.vocabulary_management.exceptions.ContentEmptyException;
import com.vocab.vocabulary_management.exceptions.FlashcardListNotExistException;
import com.vocab.vocabulary_management.exceptions.FlashcardListStillInUseException;
import com.vocab.vocabulary_management.repos.FlashcardListRepo;
import com.vocab.vocabulary_management.repos.FlashcardRepo;
import com.vocab.vocabulary_management.repos.TranslationRepo;
import com.vocab.vocabulary_management.services.FlashcardListService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class FlashcardListServiceImplIT {

    @Autowired
    FlashcardListService flashcardListService;

    @SpyBean
    FlashcardListRepo flashcardListRepo;

    @SpyBean
    FlashcardRepo flashcardRepo;

    @SpyBean
    TranslationRepo translationRepo;

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

    @AfterEach
    public void cleanUp(){
        flashcardListRepo.deleteAll();
    }

    @Test
    public void testDeleteFlashcardListOptimisticLockingHandling() throws InterruptedException, ContentEmptyException {
        // given
        flashcardListService.createFlashcardList(testContent);
        Long flashcardListId = flashcardListService.getAll().stream().filter(flashcardList -> flashcardList.getCategory().equals("holidays")).findFirst().get().getFlashcardListId();
        AtomicBoolean successDelete1 = new AtomicBoolean(false);
        AtomicBoolean successDelete2 = new AtomicBoolean(false);

        // when
        final ExecutorService executor = Executors.newFixedThreadPool(2);

        // then
        executor.execute(() -> {
            try {
                successDelete1.set(flashcardListService.deleteFlashcardList(flashcardListId));
            } catch (FlashcardListNotExistException | FlashcardListStillInUseException e) {
                throw new RuntimeException(e);
            }
        });
        executor.execute(() -> {
            try {
                successDelete2.set(flashcardListService.deleteFlashcardList(flashcardListId));
            } catch (FlashcardListNotExistException | FlashcardListStillInUseException e) {
                throw new RuntimeException(e);
            }
        });

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        assertNotEquals(successDelete1.get(), successDelete2.get());
        verify(flashcardListRepo, times(2)).deleteById(flashcardListId);
        verify(flashcardListRepo, times(2)).existsById(flashcardListId);
        verify(flashcardListRepo, times(2)).countDuelByFlashcardList(flashcardListId);

    }

}
