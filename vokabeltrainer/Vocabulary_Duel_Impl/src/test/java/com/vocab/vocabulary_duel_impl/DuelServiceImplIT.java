package com.vocab.vocabulary_duel_impl;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management.exceptions.InvalidUsernameException;
import com.vocab.user_management.exceptions.UserNotExistException;
import com.vocab.user_management.repos.UserRepo;
import com.vocab.user_management.services.UserService;
import com.vocab.vocabulary_duel_API.entities.Answer;
import com.vocab.vocabulary_duel_API.entities.Duel;
import com.vocab.vocabulary_duel_API.entities.Round;
import com.vocab.vocabulary_duel_API.exceptions.*;
import com.vocab.vocabulary_duel_API.repositories.AnswerRepo;
import com.vocab.vocabulary_duel_API.repositories.DuelRepo;
import com.vocab.vocabulary_duel_API.repositories.RoundRepo;
import com.vocab.vocabulary_duel_API.services.DuelService;
import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management.exceptions.ContentEmptyException;
import com.vocab.vocabulary_management.exceptions.FlashcardListNotExistException;
import com.vocab.vocabulary_management.repos.FlashcardListRepo;
import com.vocab.vocabulary_management.services.FlashcardListService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class DuelServiceImplIT {

    @SpyBean
    UserService userService;

    @Autowired
    FlashcardListService flashcardListService;

    @Autowired
    DuelService duelService;

    @SpyBean
    DuelRepo duelRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    FlashcardListRepo flashcardListRepo;

    @SpyBean
    RoundRepo roundRepo;

    @SpyBean
    AnswerRepo answerRepo;

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

    private UserEntity user1;
    private UserEntity user2;
    private UserEntity user3;
    private FlashcardList flashcardList;

    @BeforeEach
    public void setUp() throws ContentEmptyException, InvalidUsernameException {
        user1 = userService.createUser("testuser1");
        user2 = userService.createUser("testuser2");
        user3 = userService.createUser("testuser3");

        flashcardListService.createFlashcardList(testContent);
        flashcardList = flashcardListService.getAll().stream().filter(flashcardListTemp -> flashcardListTemp.getCategory().equals("holidays")).findFirst().get();
    }

    @AfterEach
    public void cleanUp(){
        duelRepo.deleteAll();
        flashcardListRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    public void testJoinDuelAlreadyStartedOptimisticLockingHandling() throws InterruptedException, UserNotExistException, DuelNotExistException, FlashcardListNotExistException {
        // given
        Duel duel = duelService.createDuel(user1.getUserId(), flashcardList.getFlashcardListId());
        duelService.generateRounds(duel.getDuelId());
        AtomicBoolean duelJoined1 = new AtomicBoolean(false);
        AtomicBoolean duelJoined2 = new AtomicBoolean(false);


        // when
        final ExecutorService executor = Executors.newFixedThreadPool(2);

        // then
        executor.execute(() -> {
            try {
                duelJoined1.set(duelService.joinDuel(duel.getDuelId(), user2.getUserId()));
            } catch (UserNotExistException | DuelNotExistException | DuelAlreadyStartedException | UserAlreadyPartOfDuelException e) {
                throw new RuntimeException(e);
            }
        });
        executor.execute(() -> {
            try {
                duelJoined2.set(duelService.joinDuel(duel.getDuelId(), user3.getUserId()));
            } catch (UserNotExistException | DuelNotExistException | DuelAlreadyStartedException | UserAlreadyPartOfDuelException e) {
                throw new RuntimeException(e);
            }
        });

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

//        System.out.println("duelJoined1=" + duelJoined1.get() + "; duelJoined2=" + duelJoined2.get());
        // user3 could not join because of optimistic lock exception
        assertNotEquals(duelJoined1.get(),duelJoined2.get());
        // 3 times user were retrieved by id because user1 started the duel
        verify(userService, times(3)).getById(anyLong());
        // 3 times duel was saved because user1 started the duel
        verify(duelRepo, times(3)).save(any(Duel.class));
    }

    @Test
    public void testStartDuelOptimisticLockingHandling() throws InterruptedException, UserNotExistException, DuelNotExistException, UserAlreadyPartOfDuelException, DuelAlreadyStartedException, FlashcardListNotExistException {
        // given
        Duel duel = duelService.createDuel(user1.getUserId(), flashcardList.getFlashcardListId());
        duelService.generateRounds(duel.getDuelId());
        duelService.joinDuel(duel.getDuelId(), user2.getUserId());
        duelService.joinDuel(duel.getDuelId(), user3.getUserId());
        AtomicReference<Duel> duel1 = new AtomicReference<>();
        AtomicReference<Duel> duel2 = new AtomicReference<>();

        // when
        final ExecutorService executor = Executors.newFixedThreadPool(2);

        // then
        executor.execute(() -> {
            try {
                duel1.set(duelService.startDuelRest(duel.getDuelId(), user2.getUserId()));
            } catch (UserNotExistException | DuelNotExistException | DuelAlreadyStartedException |
                     UserNotPartOfDuelException e) {
                throw new RuntimeException(e);
            }
        });
        executor.execute(() -> {
            try {
                duel2.set(duelService.startDuelRest(duel.getDuelId(), user3.getUserId()));
            } catch (UserNotExistException | DuelNotExistException | DuelAlreadyStartedException |
                     UserNotPartOfDuelException e) {
                throw new RuntimeException(e);
            }
        });

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

//        System.out.println("duelStarted1=" + duelStarted1.get() + "; duelStarted2=" + duelStarted2.get());
        assertNotEquals(duel1.get(), duel2.get());
        assertTrue(duelRepo.findById(duel.getDuelId()).get().isStarted());
        // 5 times because 1x createDuel + 2x joinDuel + 2x startDuel
        verify(duelRepo, times(5)).save(any(Duel.class));

    }

    @Test
    public void testPlayerAnswerProofNoOptimisticLocking() throws InterruptedException, UserNotExistException, DuelNotExistException, UserAlreadyPartOfDuelException, DuelAlreadyStartedException, UserNotPartOfDuelException, FlashcardListNotExistException {
        // given
        Duel duel = duelService.createDuel(user1.getUserId(), flashcardList.getFlashcardListId());
        duelService.generateRounds(duel.getDuelId());
        duelService.joinDuel(duel.getDuelId(), user2.getUserId());
        duelService.startDuel(duel.getDuelId(), user2.getUserId());
        AtomicBoolean answerSaved1 = new AtomicBoolean(false);
        AtomicBoolean answerSaved2 = new AtomicBoolean(false);
        String answer = "correctAnswer";

        // when
        final ExecutorService executor = Executors.newFixedThreadPool(2);

        // then
        executor.execute(() -> {
            try {
                answerSaved1.set(duelService.saveSelectedAnswer(answer, duel.getDuelId(), user1.getUserId()));
            } catch (UserNotExistException | DuelNotExistException | UserAlreadyPlayedRoundException e) {
                throw new RuntimeException(e);
            }
        });
        executor.execute(() -> {
            try {
                answerSaved2.set(duelService.saveSelectedAnswer(answer, duel.getDuelId(), user2.getUserId()));
            } catch (UserNotExistException | DuelNotExistException | UserAlreadyPlayedRoundException e) {
                throw new RuntimeException(e);
            }
        });

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("Anzahl an Antworten: " + duelRepo.findById(duel.getDuelId()).get().getRounds().get(0).getSelectedAnswers().size());
        assertEquals(answerSaved1.get(), answerSaved2.get());
        assertEquals(duelRepo.findById(duel.getDuelId()).get().getRounds().get(0).getSelectedAnswers().size(),2);
        // 3 times because 1x createDuel + 1x joinDuel + 1x startDuel
        verify(duelRepo, times(3)).save(any(Duel.class));
        // 13 times because 10x for generateRounds + 2x saveSelectedAnswer + 1x activateNextRound
        verify(roundRepo, times(13)).save(any(Round.class));
        verify(answerRepo, times(2)).save(any(Answer.class));
    }
}
