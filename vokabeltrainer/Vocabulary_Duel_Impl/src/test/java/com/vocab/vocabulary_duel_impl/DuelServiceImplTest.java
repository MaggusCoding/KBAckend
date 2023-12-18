package com.vocab.vocabulary_duel_impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.vocab.vocabulary_duel.entities.Answer;
import com.vocab.vocabulary_duel.entities.Round;
import com.vocab.vocabulary_duel.repositories.RoundRepo;
import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.Translation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.vocab.vocabulary_duel_impl.services.DuelServiceImpl;
import com.vocab.user_management_impl.services.UserServiceImpl;
import com.vocab.vocabulary_management_impl.services.FlashcardListServiceImpl;
import com.vocab.vocabulary_duel.repositories.DuelRepo;
import com.vocab.vocabulary_management.repos.TranslationRepo;
import com.vocab.vocabulary_duel.entities.Duel;
import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.user_management.entities.UserEntity;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class DuelServiceImplTest {

    @Mock
    private FlashcardListServiceImpl flashcardListService;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private DuelRepo duelRepo;
    @Mock
    private RoundRepo roundRepo;
    @Mock
    private Duel duel;
    @Mock
    private TranslationRepo translationRepo;
    @InjectMocks
    private DuelServiceImpl duelService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateRounds() {
        // Arrange
        Duel mockDuel = new Duel();
        FlashcardList mockFlashcardList = new FlashcardList(1L, "MockCat", "Deutsch", "Englisch", new ArrayList<>());
        mockDuel.setFlashcardsForDuel(mockFlashcardList);

        List<Translation> translations = new ArrayList<>();
        for (long i = 0; i < 20; i++) {
            translations.add(new Translation(i, new Flashcard(), "Bear" + i));
        }
        List<Flashcard> flashcards = new ArrayList<>();
        for (long i = 0; i < 20; i++) {
            flashcards.add(new Flashcard(i, "OGText" + i, mockFlashcardList, translations));
        }
        mockFlashcardList.setFlashcards(flashcards);
//
        when(translationRepo.findAll()).thenReturn(translations);
        when(duelRepo.findById(1L)).thenReturn(Optional.of(mockDuel));

        // Act
        duelService.generateRounds(1L);

        // Assert
        verify(roundRepo, times(10)).save(any(Round.class));
    }
    @Test
    public void testGetById() {
        // Arrange
        long duelId = 1L;
        Duel mockDuel = new Duel();
        mockDuel.setDuelId(duelId);

        when(duelRepo.findById(duelId)).thenReturn(Optional.of(mockDuel));

        // Act
        Optional<Duel> result = duelService.getById(duelId);

        // Assert
        assertEquals(Optional.of(mockDuel), result);
        verify(duelRepo).findById(duelId);
    }


    @Test
    public void testGetAll() {
        // Arrange
        List<Duel> mockDuels = Arrays.asList(
                new Duel(),
                new Duel(),
                new Duel()
        );

        when(duelRepo.findAll()).thenReturn(mockDuels);

        // Act
        List<Duel> result = duelService.getAll();

        // Assert
        assertEquals(mockDuels, result);
        verify(duelRepo).findAll();
    }

    @Test
    public void testCreateDuel() {
        // Arrange
        Long userId = 1L;
        Long flashcardListId = 1L;
        UserEntity mockUser = new UserEntity();
        FlashcardList mockFlashcardList = new FlashcardList();

        when(userService.getById(userId)).thenReturn(mockUser);
        when(flashcardListService.getById(flashcardListId)).thenReturn(mockFlashcardList);

        // Act
        Duel result = duelService.createDuel(userId, flashcardListId);

        // Assert
        assertNotNull(result);
        assertTrue(result.getPlayers().contains(mockUser));
        assertEquals(mockFlashcardList, result.getFlashcardsForDuel());
        assertFalse(result.isStarted());
        assertFalse(result.isFinished());

        verify(userService).getById(userId);
        verify(flashcardListService).getById(flashcardListId);
        verify(duelRepo).save(any(Duel.class));
    }

    @Test
    public void testJoinDuel() {
        // Arrange
        long duelId = 1L;
        long userId = 2L;
        Duel mockDuel = new Duel();
        mockDuel.setStarted(false);
        mockDuel.setPlayers(new ArrayList<>()); // Empty players list
        UserEntity mockUser = new UserEntity();
        mockUser.setUserId(userId);

        when(duelRepo.findById(duelId)).thenReturn(Optional.of(mockDuel));
        when(userService.getById(userId)).thenReturn(mockUser);

        // Act
        Boolean result = duelService.joinDuel(duelId, userId);

        // Assert
        assertTrue(result);
        assertTrue(mockDuel.getPlayers().contains(mockUser));

        verify(duelRepo).save(mockDuel);
        verify(duelRepo, times(2)).findById(duelId);
        verify(userService, times(2)).getById(userId);
    }
    @Test
    public void testJoinDuel_DuelAlreadyStarted() {
        // Arrange
        long duelId = 1L;
        long userId = 2L;
        Duel mockDuel = new Duel();
        mockDuel.setStarted(true); // Duel already started
        mockDuel.setPlayers(new ArrayList<>());

        when(duelRepo.findById(duelId)).thenReturn(Optional.of(mockDuel));

        // Act
        Boolean result = duelService.joinDuel(duelId, userId);

        // Assert
        assertFalse(result);
        verify(duelRepo, never()).save(any(Duel.class));
    }
    @Test
    public void testJoinDuel_UserAlreadyJoined() {
        // Arrange
        long duelId = 1L;
        long userId = 2L;
        Duel mockDuel = new Duel();
        mockDuel.setStarted(false);
        UserEntity mockUser = new UserEntity();
        mockUser.setUserId(userId);
        List<UserEntity> players = new ArrayList<>();
        players.add(mockUser);
        mockDuel.setPlayers(players); // User already in players list

        when(duelRepo.findById(duelId)).thenReturn(Optional.of(mockDuel));
        when(userService.getById(userId)).thenReturn(mockUser);

        // Act
        Boolean result = duelService.joinDuel(duelId, userId);

        // Assert
        assertFalse(result);
        verify(duelRepo, never()).save(any(Duel.class));
    }

    @Test
    public void testCalculateWinner() {
        // Arrange
        long duelId = 1L;
        Duel mockDuel = new Duel();
        List<UserEntity> players = Arrays.asList(new UserEntity(1L, "Player1"), new UserEntity(2L, "Player2"));
        mockDuel.setPlayers(players);

        List<Round> rounds = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Round round = new Round();
            round.setDuel(mockDuel);
            round.setActiveRound(i == 9); // last round active
            List<Answer> answers = new ArrayList<>();

            // Assuming first 6 rounds won by Player1, next 4 by Player2
            Answer answer = new Answer();
            answer.setPlayer(i < 6 ? players.get(0) : players.get(1));
            answer.setCorrect(true);
            answers.add(answer);

            round.setSelectedAnswers(answers);
            rounds.add(round);
        }
        mockDuel.setRounds(rounds);

        when(duelRepo.findById(duelId)).thenReturn(Optional.of(mockDuel));

        // Act
        List<UserEntity> winners = duelService.calculateWinner(duelId);

        // Assert
        assertNotNull(winners);
        assertEquals(1, winners.size());
        assertEquals(players.get(0), winners.get(0)); // Player1 should be the winner
        verify(duelRepo).findById(duelId);
    }
    @Test
    public void testStartDuel() {
        // Arrange
        long duelId = 1L;
        Duel mockDuel = new Duel();
        mockDuel.setDuelId(duelId);
        mockDuel.setStarted(false);
        List<Round> rounds = new ArrayList<>();
        Round mockRound = new Round();
        mockRound.setActiveRound(false);
        rounds.add(mockRound);
        mockDuel.setRounds(rounds);

        when(duelRepo.findById(duelId)).thenReturn(Optional.of(mockDuel));

        // Act
        boolean result = duelService.startDuel(duelId);

        // Assert
        assertTrue(result);
        assertTrue(mockDuel.isStarted());
        verify(duelRepo, times(2)).findById(duelId);
        verify(duelRepo).save(mockDuel);
    }
}
