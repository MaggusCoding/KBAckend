package com.vocab.vocabulary_duel_impl;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management.exceptions.InvalidUsernameException;
import com.vocab.user_management.exceptions.UserNotExistException;
import com.vocab.user_management_impl.services.UserServiceImpl;
import com.vocab.vocabulary_duel_API.dto.RankingPlayer;
import com.vocab.vocabulary_duel_API.entities.Answer;
import com.vocab.vocabulary_duel_API.entities.Duel;
import com.vocab.vocabulary_duel_API.entities.Round;
import com.vocab.vocabulary_duel_API.exceptions.*;
import com.vocab.vocabulary_duel_API.repositories.AnswerRepo;
import com.vocab.vocabulary_duel_API.repositories.DuelRepo;
import com.vocab.vocabulary_duel_API.repositories.RoundRepo;
import com.vocab.vocabulary_duel_impl.services.DuelServiceImpl;
import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management.entities.Translation;
import com.vocab.vocabulary_management.exceptions.FlashcardListNotExistException;
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
    public void testGenerateRounds() throws DuelNotExistException {
        // Arrange
        Duel mockDuel = new Duel();
        FlashcardList mockFlashcardList = new FlashcardList(1L, "MockCat", "Deutsch", "Englisch", new ArrayList<>(), 1L);
        mockDuel.setFlashcardsForDuel(mockFlashcardList);

        List<Translation> translations = new ArrayList<>();
        for (long i = 0; i < 20; i++) {
            translations.add(new Translation(i, new Flashcard(), "Bear" + i, 1L));
        }
        List<Flashcard> flashcards = new ArrayList<>();
        for (long i = 0; i < 20; i++) {
            flashcards.add(new Flashcard(i, "OGText" + i, mockFlashcardList, translations, 1L));
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
    public void testDeleteDuel() throws DuelNotExistException {
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
    public void testDeleteDuelExpectDuelNotExistException(){
        // Arrange
        long duelId = 1L;
        when(duelRepo.existsById(duelId)).thenReturn(false);

        // Act & Assert
        assertThrows(DuelNotExistException.class, () -> duelService.deleteDuel(duelId));
        verify(duelRepo, times(1)).existsById(duelId);
        verify(duelRepo, never()).deleteById(anyLong());
    }

    @Test
    public void testPlayRound() throws DuelNotExistException {
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
    public void testPlayRoundRest() throws RoundNotExistException {
        // Arrange
        long duelId = 1L;
        Duel mockDuel = mock(Duel.class, RETURNS_DEEP_STUBS);
        when(duelRepo.findById(duelId)).thenReturn(Optional.of(mockDuel));

        Round currentRound = mock(Round.class);
        Flashcard mockFlashcard = mock(Flashcard.class);
        Translation mockTranslation = mock(Translation.class);
        List<Translation> translations = Collections.singletonList(mockTranslation);

        when(roundRepo.findById(any())).thenReturn(Optional.ofNullable(currentRound));
        when(mockDuel.getRounds()).thenReturn(Collections.singletonList(currentRound));
        when(currentRound.getQuestionedFlashcard()).thenReturn(mockFlashcard);
        when(mockFlashcard.getOriginalText()).thenReturn("Question");
        when(mockFlashcard.getTranslations()).thenReturn(translations);
        when(mockTranslation.getTranslationText()).thenReturn("Correct Answer");
        when(currentRound.getWrongAnswers()).thenReturn("Wrong Answer 1;Wrong Answer 2");

        // Act
        List<String> result = duelService.playRoundRest(currentRound.getRoundId());

        // Assert
        assertNotNull(result);

        assertTrue(result.contains("Question"));
        assertTrue(result.contains("Wrong Answer 1"));
        assertTrue(result.contains("Wrong Answer 2"));
        assertTrue(result.contains("Correct Answer"));

        verify(currentRound, atLeastOnce()).getQuestionedFlashcard();
        verify(mockFlashcard, atLeastOnce()).getOriginalText();
        verify(mockFlashcard, atLeastOnce()).getTranslations();
        verify(mockTranslation, atLeastOnce()).getTranslationText();
        verify(currentRound, atLeastOnce()).getWrongAnswers();
    }

    @Test
    public void testSaveSelectedAnswer() throws UserNotExistException, DuelNotExistException, UserAlreadyPlayedRoundException {
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
        mockRound.setSelectedAnswers(new ArrayList<>());

        when(duelRepo.findById(duelId)).thenReturn(Optional.of(mockDuel));
        when(roundRepo.findRoundByDuelAndActiveRoundTrue(mockDuel)).thenReturn(mockRound);
        when(userService.getById(playerId)).thenReturn(mockUser);

        // Act
        duelService.saveSelectedAnswer("Answer", duelId, playerId);

        // Assert
        verify(answerRepo).save(any(Answer.class));
    }

    @Test
    public void testSaveSelectedAnswerExpectUserAlreadyPlayedRoundException() throws UserNotExistException {
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

        // Create a mock Answer
        Answer mockAnswer = new Answer();
        mockAnswer.setPlayer(mockUser);
        mockAnswer.setCorrect(true);
        mockAnswer.setRound(mockRound);

        mockRound.setSelectedAnswers(List.of(mockAnswer));


        when(duelRepo.findById(duelId)).thenReturn(Optional.of(mockDuel));
        when(roundRepo.findRoundByDuelAndActiveRoundTrue(mockDuel)).thenReturn(mockRound);
        when(userService.getById(playerId)).thenReturn(mockUser);

        // Act & Assert
        assertThrows(UserAlreadyPlayedRoundException.class, () -> duelService.saveSelectedAnswer("Answer", duelId, playerId));
        verify(answerRepo, never()).save(any(Answer.class));
    }

    @Test
    public void testSaveSelectedAnswerRest() throws UserNotExistException, UserAlreadyPlayedRoundException, RoundNotExistException {
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
        mockRound.setSelectedAnswers(new ArrayList<>());
        mockDuel.setPlayers(List.of(mockUser));
        mockDuel.setRounds(List.of(mockRound));

        when(roundRepo.findById(mockRound.getRoundId())).thenReturn(Optional.of(mockRound));
        when(userService.getById(playerId)).thenReturn(mockUser);

        // Act
        duelService.saveSelectedAnswerRest("Answer", mockRound.getRoundId(), playerId);

        // Assert
        verify(answerRepo).save(any(Answer.class));
        verify(duelRepo).save(any(Duel.class));
    }

    @Test
    public void testSaveSelectedAnswerRestExpectUserAlreadyPlayedRoundException() throws UserNotExistException {
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
        mockDuel.setPlayers(List.of(mockUser));
        mockDuel.setRounds(List.of(mockRound));

        // Create a mock Answer
        Answer mockAnswer = new Answer();
        mockAnswer.setPlayer(mockUser);
        mockAnswer.setCorrect(true);
        mockAnswer.setRound(mockRound);

        mockRound.setSelectedAnswers(List.of(mockAnswer));


        when(roundRepo.findById(mockRound.getRoundId())).thenReturn(Optional.of(mockRound));
        when(userService.getById(playerId)).thenReturn(mockUser);

        // Act
        assertThrows(UserAlreadyPlayedRoundException.class, () -> duelService.saveSelectedAnswerRest("Answer", mockRound.getRoundId(), playerId));

        // Assert
        verify(answerRepo, never()).save(any(Answer.class));
    }

    @Test
    public void testActivateNextRound() throws DuelNotExistException {
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
    public void testActivateNextRoundWhenNotLastRound() throws DuelNotExistException {
        // Arrange
        long duelId = 1L;
        Duel mockDuel = new Duel();
        mockDuel.setDuelId(duelId);

        Round currentRound = new Round();
        currentRound.setDuel(mockDuel);
        currentRound.setActiveRound(true);

        Round nextRound = new Round();
        currentRound.setDuel(mockDuel);
        currentRound.setActiveRound(false);

        // Simulate that there are no more rounds to play
        when(duelRepo.findById(duelId)).thenReturn(Optional.of(mockDuel));
        when(roundRepo.findRoundByDuelAndActiveRoundTrue(mockDuel)).thenReturn(currentRound);
        when(roundRepo.findFirstByDuelAndSelectedAnswersEmpty(mockDuel)).thenReturn(nextRound); // No next round

        // Act
        duelService.activateNextRound(duelId);

        // Assert
        assertFalse(currentRound.isActiveRound()); // Current round should be deactivated
        verify(roundRepo).findRoundByDuelAndActiveRoundTrue(mockDuel);
        verify(roundRepo, times(1)).saveAll(any());
        verify(duelRepo, never()).save(any());
    }

    @Test
    public void testDuelsToJoin() throws UserNotExistException {
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
    public void testDuelsToStart() throws UserNotExistException {
        // Arrange
        UserEntity mockUser = new UserEntity();
        mockUser.setUserId(1L);

        Duel mockDuel1 = new Duel();
        mockDuel1.setDuelId(1L);
        mockDuel1.setStarted(false);
        mockDuel1.setFinished(false);
        mockDuel1.setPlayer(mockUser);

        when(duelRepo.findDuelsByStartedIsFalseAndFinishedIsFalse()).thenReturn(List.of(mockDuel1));
        when(userService.getById(mockUser.getUserId())).thenReturn(mockUser);

        // Act
        List<Duel> result = duelService.duelsToStart(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        mockDuel1.setStarted(true);
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
    public void testCreateDuel() throws UserNotExistException, FlashcardListNotExistException {
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
    public void testJoinDuel() throws UserNotExistException, UserAlreadyPartOfDuelException, DuelAlreadyStartedException, DuelNotExistException {
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
        verify(duelRepo, times(1)).findById(duelId);
        verify(userService, times(1)).getById(userId);
    }
    @Test
    public void testJoinDuelExpectDuelAlreadyStartedException() {
        // Arrange
        long duelId = 1L;
        long userId = 2L;
        Duel mockDuel = new Duel();
        mockDuel.setStarted(true); // Duel already started
        mockDuel.setPlayers(new ArrayList<>());

        when(duelRepo.findById(duelId)).thenReturn(Optional.of(mockDuel));

        // Act & Assert
        assertThrows(DuelAlreadyStartedException.class, () -> duelService.joinDuel(duelId, userId));
        verify(duelRepo, never()).save(any(Duel.class));
    }
    @Test
    public void testJoinDuelExpectUserAlreadyPartOfDuelException() throws UserNotExistException {
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

        // Act & Assert
        assertThrows(UserAlreadyPartOfDuelException.class, () -> duelService.joinDuel(duelId, userId));
        verify(duelRepo, never()).save(any(Duel.class));
    }

    @Test
    public void testCalculateWinner() throws UserNotExistException, DuelNotExistException {
        // Arrange
        long duelId = 1L;
        Duel mockDuel = new Duel();
        List<UserEntity> players = Arrays.asList(new UserEntity(1L, "Player1",1L), new UserEntity(2L, "Player2", 1L));
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
        when(duelRepo.getRankingOfDuel(duelId)).thenReturn(List.of(new RankingPlayer(players.get(0).getUserId(), 6L)));
        when(userService.getById(anyLong())).thenReturn(players.get(0));

        // Act
        List<UserEntity> winners = duelService.calculateWinner(duelId);

        // Assert
        assertNotNull(winners);
        assertEquals(1, winners.size());
        assertEquals(players.get(0), winners.get(0)); // Player1 should be the winner
        verify(duelRepo).findById(duelId);
        verify(duelRepo).getRankingOfDuel(duelId);
        verify(userService).getById(1L);
    }

    @Test
    public void testCalculateWinnerNobodyWins() throws DuelNotExistException, UserNotExistException, InvalidUsernameException {
        // Arrange
        long duelId = 1L;
        Duel mockDuel = new Duel();
        List<UserEntity> players = Arrays.asList(new UserEntity(1L, "Player1", 1L), new UserEntity(2L, "Player2",1L));
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
    public void calculateWinnerWhenWinnerAlreadySet() throws DuelNotExistException {
        // Arrange
        long duelId = 1L;
        Duel mockDuel = new Duel();
        List<UserEntity> players = Arrays.asList(new UserEntity(1L, "Player1",1L), new UserEntity(2L, "Player2", 1L));
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
        mockDuel.setWinner(List.of(players.get(1)));

        when(duelRepo.findById(duelId)).thenReturn(Optional.of(mockDuel));
        when(duelRepo.getRankingOfDuel(duelId)).thenReturn(List.of(new RankingPlayer(players.get(0).getUserId(), 6L)));

        // Act
        List<UserEntity> winners = duelService.calculateWinner(duelId);

        // Assert
        assertNotNull(winners);
        assertEquals(1, winners.size());
        assertEquals(players.get(1), winners.get(0)); // Player1 should be the winner
        verify(duelRepo).findById(duelId);
        verify(duelRepo).getRankingOfDuel(duelId);

    }

    @Test
    public void testStartDuel() throws UserNotExistException, UserNotPartOfDuelException, DuelAlreadyStartedException, DuelNotExistException {
        // Arrange
        long duelId = 1L;
        UserEntity mockUser = new UserEntity(1L, "testuser", 1L);
        Duel mockDuel = new Duel();
        mockDuel.setDuelId(duelId);
        mockDuel.setStarted(false);
        mockDuel.setPlayers(List.of(mockUser));
        List<Round> rounds = new ArrayList<>();
        Round mockRound = new Round();
        mockRound.setActiveRound(false);
        rounds.add(mockRound);
        mockDuel.setRounds(rounds);

        when(duelRepo.findById(duelId)).thenReturn(Optional.of(mockDuel));
        when(userService.getById(mockUser.getUserId())).thenReturn(mockUser);

        // Act
        boolean result = duelService.startDuel(duelId, mockUser.getUserId());

        // Assert
        assertTrue(result);
        assertTrue(mockDuel.isStarted());
        verify(duelRepo, times(2)).findById(duelId);
        verify(duelRepo).save(mockDuel);
    }

    @Test
    public void testStartDuelExpectDuelAlreadyStartedException() throws UserNotExistException {
        // Arrange
        long duelId = 1L;
        UserEntity mockUser = new UserEntity(1L, "testuser", 1L);
        Duel mockDuel = new Duel();
        mockDuel.setDuelId(duelId);
        mockDuel.setStarted(true);
        mockDuel.setPlayers(List.of(mockUser));
        List<Round> rounds = new ArrayList<>();
        Round mockRound = new Round();
        mockRound.setActiveRound(false);
        rounds.add(mockRound);
        mockDuel.setRounds(rounds);

        when(duelRepo.findById(duelId)).thenReturn(Optional.of(mockDuel));
        when(userService.getById(mockUser.getUserId())).thenReturn(mockUser);

        // Act & Assert
        assertThrows(DuelAlreadyStartedException.class, () -> duelService.startDuel(duelId, mockUser.getUserId()));
        verify(duelRepo, times(2)).findById(duelId);
        verify(duelRepo, never()).save(any(Duel.class));
    }

    @Test
    public void testStartDuelExpectDuelNotExistException() {
        // Arrange
        long duelId = 1L;
        UserEntity mockUser = new UserEntity(1L, "testuser", 1L);
        when(duelRepo.findById(duelId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DuelNotExistException.class, () -> duelService.startDuel(duelId, mockUser.getUserId()));
        verify(duelRepo, times(1)).findById(duelId);
        verify(duelRepo, never()).save(any(Duel.class));
    }

    @Test
    public void testStartDuelExpectUserNotPartOfDuelException() throws UserNotExistException {
        // Arrange
        long duelId = 1L;
        UserEntity mockUser = new UserEntity(1L, "testuser", 1L);
        UserEntity mockUser2 = new UserEntity(2L, "testuser2", 1L);
        Duel mockDuel = new Duel();
        mockDuel.setDuelId(duelId);
        mockDuel.setStarted(false);
        mockDuel.setPlayers(List.of(mockUser2));
        List<Round> rounds = new ArrayList<>();
        Round mockRound = new Round();
        mockRound.setActiveRound(false);
        rounds.add(mockRound);
        mockDuel.setRounds(rounds);

        when(duelRepo.findById(duelId)).thenReturn(Optional.of(mockDuel));
        when(userService.getById(mockUser.getUserId())).thenReturn(mockUser);

        // Act & Assert
        assertThrows(UserNotPartOfDuelException.class, () -> duelService.startDuel(duelId, mockUser.getUserId()));
        verify(duelRepo, times(2)).findById(duelId);
        verify(duelRepo, never()).save(any(Duel.class));
    }

    @Test
    public void testStartDuelRest() throws UserNotExistException, UserNotPartOfDuelException, DuelAlreadyStartedException, DuelNotExistException {
        // Arrange
        long duelId = 1L;
        UserEntity mockUser = new UserEntity(1L, "testuser", 1L);
        Duel mockDuel = new Duel();
        mockDuel.setDuelId(duelId);
        mockDuel.setStarted(false);
        mockDuel.setPlayers(List.of(mockUser));
        List<Round> rounds = new ArrayList<>();
        Round mockRound = new Round();
        mockRound.setActiveRound(false);
        rounds.add(mockRound);
        mockDuel.setRounds(rounds);

        when(duelRepo.findById(duelId)).thenReturn(Optional.of(mockDuel));
        when(userService.getById(mockUser.getUserId())).thenReturn(mockUser);
        when(duelRepo.save(any(Duel.class))).thenReturn(mockDuel);

        // Act
        Duel result = duelService.startDuelRest(duelId, mockUser.getUserId());

        // Assert
        assertNotNull(result);
        assertTrue(mockDuel.isStarted());
        assertTrue(result.isStarted());
        verify(duelRepo, times(2)).findById(duelId);
        verify(duelRepo).save(mockDuel);
    }

    @Test
    public void testStartDuelRestExpectDuelAlreadyStartedException() throws UserNotExistException {
        // Arrange
        long duelId = 1L;
        UserEntity mockUser = new UserEntity(1L, "testuser", 1L);
        Duel mockDuel = new Duel();
        mockDuel.setDuelId(duelId);
        mockDuel.setStarted(true);
        mockDuel.setPlayers(List.of(mockUser));
        List<Round> rounds = new ArrayList<>();
        Round mockRound = new Round();
        mockRound.setActiveRound(false);
        rounds.add(mockRound);
        mockDuel.setRounds(rounds);

        when(duelRepo.findById(duelId)).thenReturn(Optional.of(mockDuel));
        when(userService.getById(mockUser.getUserId())).thenReturn(mockUser);

        // Act & Assert
        assertThrows(DuelAlreadyStartedException.class, () -> duelService.startDuelRest(duelId, mockUser.getUserId()));
        verify(duelRepo, times(2)).findById(duelId);
        verify(duelRepo, never()).save(any(Duel.class));
    }

    @Test
    public void testStartDuelRestExpectDuelNotExistException() {
        // Arrange
        long duelId = 1L;
        UserEntity mockUser = new UserEntity(1L, "testuser", 1L);
        when(duelRepo.findById(duelId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DuelNotExistException.class, () -> duelService.startDuelRest(duelId, mockUser.getUserId()));
        verify(duelRepo, times(1)).findById(duelId);
        verify(duelRepo, never()).save(any(Duel.class));
    }

    @Test
    public void testStartDuelRestExpectUserNotPartOfDuelException() throws UserNotExistException {
        // Arrange
        long duelId = 1L;
        UserEntity mockUser = new UserEntity(1L, "testuser", 1L);
        UserEntity mockUser2 = new UserEntity(2L, "testuser2", 1L);
        Duel mockDuel = new Duel();
        mockDuel.setDuelId(duelId);
        mockDuel.setStarted(false);
        mockDuel.setPlayers(List.of(mockUser2));
        List<Round> rounds = new ArrayList<>();
        Round mockRound = new Round();
        mockRound.setActiveRound(false);
        rounds.add(mockRound);
        mockDuel.setRounds(rounds);

        when(duelRepo.findById(duelId)).thenReturn(Optional.of(mockDuel));
        when(userService.getById(mockUser.getUserId())).thenReturn(mockUser);

        // Act & Assert
        assertThrows(UserNotPartOfDuelException.class, () -> duelService.startDuelRest(duelId, mockUser.getUserId()));
        verify(duelRepo, times(2)).findById(duelId);
        verify(duelRepo, never()).save(any(Duel.class));
    }

    @Test
    public void testGetNotPlayedRounds() throws UserNotExistException, DuelNotExistException {
        // Arrange
        long duelId = 1L;
        UserEntity mockUser = new UserEntity(1L, "testuser", 1L);
        UserEntity mockUser2 = new UserEntity(2L, "testuser2", 1L);
        Duel mockDuel = new Duel();
        mockDuel.setDuelId(duelId);
        mockDuel.setStarted(false);
        mockDuel.setPlayers(List.of(mockUser, mockUser2));
        List<Round> rounds = new ArrayList<>();
        Round mockRound = new Round();
        Round mockRound2 = new Round();
        Round mockRound3 = new Round();
        rounds.addAll(List.of(mockRound, mockRound2, mockRound3));
        mockDuel.setRounds(rounds);


        when(duelRepo.findById(any())).thenReturn(Optional.of(mockDuel));
        when(userService.getById(any())).thenReturn(mockUser);
        when(roundRepo.findRoundsByDuelAndNotPlayedByUser(mockDuel, mockUser)).thenReturn(List.of(mockRound2, mockRound3));

        List<Round> result = duelService.getNotPlayedRounds(mockDuel.getDuelId(), mockUser.getUserId());

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(List.of(mockRound2, mockRound3), result);

    }

    @Test
    public void testDuelsToPlay() throws UserNotExistException {
        // Arrange
        UserEntity mockUser = new UserEntity();
        mockUser.setUserId(1L);

        Duel mockDuel2 = new Duel();
        mockDuel2.setDuelId(2L);
        mockDuel2.setStarted(true);
        mockDuel2.setFinished(false);
        mockDuel2.setPlayer(mockUser);

        when(duelRepo.findDuelsByStartedIsTrueAndFinishedIsFalseAndPlayersContaining(any())).thenReturn(List.of(mockDuel2));
        when(userService.getById(mockUser.getUserId())).thenReturn(mockUser);

        // Act
        List<Duel> result = duelService.duelsToPlay(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockDuel2, result.get(0));
    }

}

