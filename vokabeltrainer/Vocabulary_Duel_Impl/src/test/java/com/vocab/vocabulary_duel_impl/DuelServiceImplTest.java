package com.vocab.vocabulary_duel_impl;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management_impl.services.UserServiceImpl;
import com.vocab.vocabulary_duel_API.dto.RankingPlayer;
import com.vocab.vocabulary_duel_API.entities.Answer;
import com.vocab.vocabulary_duel_API.entities.Duel;
import com.vocab.vocabulary_duel_API.entities.Round;
import com.vocab.vocabulary_duel_API.repositories.AnswerRepo;
import com.vocab.vocabulary_duel_API.repositories.DuelRepo;
import com.vocab.vocabulary_duel_API.repositories.RoundRepo;
import com.vocab.vocabulary_duel_impl.services.DuelServiceImpl;
import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management.entities.Translation;
import com.vocab.vocabulary_management.repos.TranslationRepo;
import com.vocab.vocabulary_management_impl.services.FlashcardListServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    private TranslationRepo translationRepo;
    @Mock
    private AnswerRepo answerRepo;
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
    public void testDeleteDuel() {
        // Arrange
        long duelId = 1L;
        when(duelRepo.existsById(duelId)).thenReturn(true);

        // Act
        boolean result = duelService.deleteDuel(duelId);

        // Assert
        assertTrue(result);
        verify(duelRepo).existsById(duelId);
        verify(duelRepo).deleteById(duelId);
    }
    @Test
    public void testPlayRound() {
        // Arrange
        long duelId = 1L;
        Duel mockDuel = mock(Duel.class, RETURNS_DEEP_STUBS);
        when(duelRepo.findById(duelId)).thenReturn(Optional.of(mockDuel));

        Round activeRound = mock(Round.class);
        Flashcard mockFlashcard = mock(Flashcard.class);
        Translation mockTranslation = mock(Translation.class);
        List<Translation> translations = Collections.singletonList(mockTranslation);

        when(mockDuel.getRounds()).thenReturn(Collections.singletonList(activeRound));
        when(activeRound.isActiveRound()).thenReturn(true);
        when(activeRound.getQuestionedFlashcard()).thenReturn(mockFlashcard);
        when(mockFlashcard.getOriginalText()).thenReturn("Question");
        when(mockFlashcard.getTranslations()).thenReturn(translations);
        when(mockTranslation.getTranslationText()).thenReturn("Correct Answer");
        when(activeRound.getWrongAnswers()).thenReturn("Wrong Answer 1;Wrong Answer 2");

        // Act
        List<String> result = duelService.playRound(duelId);

        // Assert
        assertNotNull(result);

        assertTrue(result.contains("Question"));
        assertTrue(result.contains("Wrong Answer 1"));
        assertTrue(result.contains("Wrong Answer 2"));
        assertTrue(result.contains("Correct Answer"));

        verify(duelRepo).findById(duelId);
        verify(activeRound, atLeastOnce()).getQuestionedFlashcard();
        verify(mockFlashcard, atLeastOnce()).getOriginalText();
        verify(mockFlashcard, atLeastOnce()).getTranslations();
        verify(mockTranslation, atLeastOnce()).getTranslationText();
        verify(activeRound, atLeastOnce()).getWrongAnswers();
    }


    @Test
    public void testSaveSelectedAnswer() {
        // Arrange
        Long duelId = 1L;
        Long playerId = 1L;

        // Create a mock Duel
        Duel mockDuel = new Duel();
        mockDuel.setDuelId(duelId);

        // Create a mock UserEntity
        UserEntity mockUser = new UserEntity();
        mockUser.setUserId(playerId);

        // Create a mock Translation
        Translation mockTranslation = new Translation();
        mockTranslation.setTranslationText("Answer");

        // Create a mock Flashcard with a Translation
        Flashcard mockFlashcard = new Flashcard();
        mockFlashcard.setOriginalText("Question");
        mockFlashcard.setTranslations(Collections.singletonList(mockTranslation));

        // Create a mock Round with a valid questionedFlashcard
        Round mockRound = new Round();
        mockRound.setDuel(mockDuel);
        mockRound.setQuestionedFlashcard(mockFlashcard);

        when(duelRepo.findById(duelId)).thenReturn(Optional.of(mockDuel));
        when(roundRepo.findRoundByDuelAndActiveRoundTrue(mockDuel)).thenReturn(mockRound);
        when(userService.getById(playerId)).thenReturn(mockUser);

        // Act
        duelService.saveSelectedAnswer("Answer", duelId, playerId);

        // Assert
        verify(answerRepo).save(any(Answer.class));
    }
    @Test
    public void testActivateNextRound() {
        // Arrange
        long duelId = 1L;
        Duel mockDuel = new Duel();
        mockDuel.setDuelId(duelId);

        Round currentRound = new Round();
        currentRound.setDuel(mockDuel);
        currentRound.setActiveRound(true);

        // Simulate that there are no more rounds to play
        when(duelRepo.findById(duelId)).thenReturn(Optional.of(mockDuel));
        when(roundRepo.findRoundByDuelAndActiveRoundTrue(mockDuel)).thenReturn(currentRound);
        when(roundRepo.findFirstByDuelAndSelectedAnswersEmpty(mockDuel)).thenReturn(null); // No next round

        // Act
        duelService.activateNextRound(duelId);

        // Assert
        assertFalse(currentRound.isActiveRound()); // Current round should be deactivated
        verify(roundRepo).findRoundByDuelAndActiveRoundTrue(mockDuel);

        // Verify that when there are no more rounds, the duel is set as finished
        verify(roundRepo, never()).save(any(Round.class)); // No rounds saved when there are no more rounds to play
        verify(duelRepo).save(mockDuel); // Duel should be saved to set it as finished
    }

    @Test
    public void testDuelsToJoin() {
        // Arrange
        UserEntity mockUser = new UserEntity();
        mockUser.setUserId(1L);

        Duel mockDuel1 = new Duel();
        mockDuel1.setStarted(false);
        mockDuel1.setFinished(false);

        Duel mockDuel2 = new Duel();
        mockDuel2.setStarted(true);
        mockDuel2.setFinished(false);
        mockDuel2.setPlayer(mockUser);

        List<Duel> mockDuels = Arrays.asList(mockDuel1, mockDuel2);

        when(duelRepo.findDuelsByStartedIsFalseAndFinishedIsFalse()).thenReturn(mockDuels);
        when(userService.getById(mockUser.getUserId())).thenReturn(mockUser);

        // Act
        List<Duel> result = duelService.duelsToJoin(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size()); // Only the first duel is eligible to join
        assertEquals(mockDuel1, result.get(0));

        verify(duelRepo).findDuelsByStartedIsFalseAndFinishedIsFalse();
    }

    @Test
    public void testDuelsToStart() {
        // Arrange
        UserEntity mockUser = new UserEntity();
        mockUser.setUserId(1L);

        Duel mockDuel1 = new Duel();
        mockDuel1.setStarted(false);
        mockDuel1.setFinished(false);

        Duel mockDuel2 = new Duel();
        mockDuel2.setStarted(true);
        mockDuel2.setFinished(false);
        mockDuel2.setPlayer(mockUser);

        List<Duel> mockDuels = Arrays.asList(mockDuel1, mockDuel2);

        when(duelRepo.findDuelsByStartedIsFalseAndFinishedIsFalse()).thenReturn(mockDuels);
        when(userService.getById(mockUser.getUserId())).thenReturn(mockUser);

        // Act
        List<Duel> result = duelService.duelsToJoin(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size()); // Only the first duel is eligible to join
        assertEquals(mockDuel1, result.get(0));

        verify(duelRepo).findDuelsByStartedIsFalseAndFinishedIsFalse();
    }
    @Test
    public void testGenerateWrongAnswers() {
        // Arrange
        String correctAnswer = "CorrectAnswer";
        List<String> allTranslationStrings = Arrays.asList("CorrectAnswerr", "CorrectAnswerrr", "CorrectAnswerrrr", "CorrectAnswerrrrrr", "CorrectAnswerrrrrrr", "CorrectAnswerrrrrrr");

        // Act
        List<String> result = duelService.generateWrongAnswers(correctAnswer, allTranslationStrings);

        // Assert
        assertEquals(List.of("CorrectAnswerr","CorrectAnswerrr","CorrectAnswerrrr"),result);

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
        when(duelRepo.getRankingOfDuel(duelId)).thenReturn(List.of(new RankingPlayer(players.get(0).getUsername(), 6L)));
        when(userService.findByUsername(any())).thenReturn(Optional.ofNullable(players.get(0)));

        // Act
        List<UserEntity> winners = duelService.calculateWinner(duelId);

        // Assert
        assertNotNull(winners);
        assertEquals(1, winners.size());
        assertEquals(players.get(0), winners.get(0)); // Player1 should be the winner
        verify(duelRepo).findById(duelId);
        verify(duelRepo).getRankingOfDuel(duelId);
        verify(userService).findByUsername("Player1");
    }

    @Test
    public void testCalculateWinnerNobodyWins(){
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

            // Assuming all rounds users answers wrong
            Answer answer1 = new Answer();
            answer1.setPlayer(players.get(0));
            answer1.setCorrect(false);
            Answer answer2 = new Answer();
            answer2.setPlayer(players.get(1));
            answer2.setCorrect(false);
            answers.add(answer1);
            answers.add(answer2);

            round.setSelectedAnswers(answers);
            rounds.add(round);
        }

        when(duelRepo.findById(duelId)).thenReturn(Optional.of(mockDuel));
        when(duelRepo.getRankingOfDuel(duelId)).thenReturn(List.of());

        // Act
        List<UserEntity> winners = duelService.calculateWinner(duelId);

        // Assert
        assertNotNull(winners);
        assertEquals(0, winners.size());
        verify(duelRepo).findById(duelId);
        verify(duelRepo).getRankingOfDuel(duelId);
        verify(userService, times(0)).findByUsername(any());
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
