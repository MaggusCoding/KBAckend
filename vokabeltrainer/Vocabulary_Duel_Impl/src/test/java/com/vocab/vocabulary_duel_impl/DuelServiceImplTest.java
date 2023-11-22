package com.vocab.vocabulary_duel_impl;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.vocabulary_duel.entities.Answer;
import com.vocab.vocabulary_duel.entities.Duel;
import com.vocab.vocabulary_duel.entities.Round;
import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management.entities.Translation;
import com.vocab.vocabulary_duel_impl.services.DuelServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DuelServiceImplTest {

    DuelServiceImpl service = new DuelServiceImpl();

    @Test
    void testCreateDuelExpectOk(){
        UserEntity userEntity = new UserEntity(1L, "user1");
        Flashcard flashcard = new Flashcard(1L, "english word", null, null);
        Translation translation = new Translation(1L,flashcard,"deutsches Wort");
        flashcard.setTranslations(List.of(translation));
        Duel duel = new Duel(1L, List.of(userEntity), List.of(flashcard));

        service.createDuel(duel);

        Duel duel1 = service.getById(1L);
        assertThat(duel1).isNotNull().usingRecursiveComparison().isEqualTo(duel);
    }

    @Test
    void testGenerateFlashcardListExpect10(){
        UserEntity userEntity = new UserEntity(1L, "user1");
        Flashcard flashcard = new Flashcard(1L, "english word", null, null);
        Translation translation = new Translation(1L,flashcard,"deutsches Wort");
        flashcard.setTranslations(List.of(translation));
        FlashcardList flashcardList = new FlashcardList(1L, "This is london", "english", "deutsch", List.of(flashcard, flashcard, flashcard, flashcard, flashcard, flashcard, flashcard, flashcard, flashcard, flashcard));
        Duel duel = new Duel(1L, List.of(userEntity), List.of(flashcard));

        List<Flashcard> flashcardsForDuel = service.generateFlashcardList(flashcardList, duel);

        assertThat(flashcardsForDuel).isNotNull().hasSize(10);
    }

    @Test
    void testGenerateFlashcardFromNonExistingListExpectEmptyResult(){
        UserEntity userEntity = new UserEntity(1L, "user1");
        Flashcard flashcard = new Flashcard(1L, "english word", null, null);
        Translation translation = new Translation(1L,flashcard,"deutsches Wort");
        flashcard.setTranslations(List.of(translation));
        FlashcardList flashcardList = new FlashcardList(1L, "This is london", "english", "deutsch", List.of(flashcard, flashcard, flashcard, flashcard, flashcard, flashcard, flashcard, flashcard, flashcard, flashcard));
        Duel duel = new Duel(1L, List.of(userEntity), List.of(flashcard));

        FlashcardList flashcardList2 = new FlashcardList(2L, "This is latin america", "english", "deutsch", List.of(flashcard, flashcard, flashcard, flashcard, flashcard, flashcard, flashcard, flashcard, flashcard, flashcard));

        List<Flashcard> flashcardsForDuel = service.generateFlashcardList(flashcardList2, duel);

        assertThat(flashcardsForDuel).isNotNull().isEmpty();
    }

    @Test
    void testCalculateWinnerExpect1Winner(){
        UserEntity userEntity = new UserEntity(1L, "user1");
        UserEntity userEntity2 = new UserEntity(2L, "user2");
        Flashcard flashcard = new Flashcard(1L, "english word", null, null);
        Flashcard flashcard2= new Flashcard(2L, "amazing", null, null);
        Translation translation = new Translation(1L,flashcard,"deutsches Wort");
        Translation translation2 = new Translation(2L,flashcard,"erstaunlich");
        flashcard.setTranslations(List.of(translation));
        flashcard2.setTranslations(List.of(translation2));
        Duel duel = new Duel(1L, List.of(userEntity, userEntity2), List.of(flashcard,flashcard2));
        Round round = new Round(1L, duel, flashcard, null, List.of("parola italiana", "mot français", "palavra portuguesa"));
        Round round2 = new Round(2L, duel, flashcard, null, List.of("mies", "wasser", "amazon"));
        Answer answer = new Answer(1L, userEntity, flashcard, round, "deutsches Wort");
        answer.setCorrect(true);
        Answer answer2 = new Answer(2L, userEntity2, flashcard, round, "deutsches Wort");
        answer2.setCorrect(true);
        Answer answer3 = new Answer(3L, userEntity, flashcard2, round2, "erstaunlich");
        answer3.setCorrect(true);
        Answer answer4 = new Answer(4L, userEntity2, flashcard2, round2, "amazon");
        answer4.setCorrect(false);
        round.setSelectedAnswers(List.of(answer, answer2));
        round2.setSelectedAnswers(List.of(answer3, answer4));

        List<UserEntity> winner = service.calculateWinner(duel);

        assertThat(winner).isNotNull().hasSize(1);
        assertThat(winner).extracting("username").contains("user1");

    }

    @Test
    void testCalculateWinnerExpect2Winner(){
        UserEntity userEntity = new UserEntity(1L, "user1");
        UserEntity userEntity2 = new UserEntity(2L, "user2");
        Flashcard flashcard = new Flashcard(1L, "english word", null, null);
        Flashcard flashcard2= new Flashcard(2L, "amazing", null, null);
        Translation translation = new Translation(1L,flashcard,"deutsches Wort");
        Translation translation2 = new Translation(2L,flashcard,"erstaunlich");
        flashcard.setTranslations(List.of(translation));
        flashcard2.setTranslations(List.of(translation2));
        Duel duel = new Duel(1L, List.of(userEntity, userEntity2), List.of(flashcard,flashcard2));
        Round round = new Round(1L, duel, flashcard, null, List.of("parola italiana", "mot français", "palavra portuguesa"));
        Round round2 = new Round(2L, duel, flashcard, null, List.of("mies", "wasser", "amazon"));
        Answer answer = new Answer(1L, userEntity, flashcard, round, "deutsches Wort");
        answer.setCorrect(true);
        Answer answer2 = new Answer(2L, userEntity2, flashcard, round, "deutsches Wort");
        answer2.setCorrect(true);
        Answer answer3 = new Answer(3L, userEntity, flashcard2, round2, "erstaunlich");
        answer3.setCorrect(true);
        Answer answer4 = new Answer(4L, userEntity2, flashcard2, round2, "amazon");
        answer4.setCorrect(true);
        round.setSelectedAnswers(List.of(answer, answer2));
        round2.setSelectedAnswers(List.of(answer3, answer4));

        List<UserEntity> winner = service.calculateWinner(duel);

        assertThat(winner).isNotNull().hasSize(2);
        assertThat(winner).extracting("username").contains("user1", "user2");
    }

    @Test
    void testJoinDuelExpectOk(){
        UserEntity userEntity = new UserEntity(1L, "user1");
        UserEntity userEntity2 = new UserEntity(2L, "user2");
        Flashcard flashcard = new Flashcard(1L, "english word", null, null);
        Flashcard flashcard2= new Flashcard(2L, "amazing", null, null);
        Translation translation = new Translation(1L,flashcard,"deutsches Wort");
        Translation translation2 = new Translation(2L,flashcard,"erstaunlich");
        flashcard.setTranslations(List.of(translation));
        flashcard2.setTranslations(List.of(translation2));
        Duel duel = new Duel(1L, List.of(userEntity), List.of(flashcard,flashcard2));

        assertThat(duel.getPlayers()).hasSize(1).extracting("username").contains("user1");

        service.joinDuel(duel.getDuelId(), userEntity2.getUserId());

        Duel currentDuel = service.getById(duel.getDuelId());
        assertThat(currentDuel).isNotNull();
        assertThat(currentDuel.getPlayers()).hasSize(2).extracting("username").contains("user1", "user2");

    }

    @Test
    void testJoinDuelWhenAlreadyIn(){
        UserEntity userEntity = new UserEntity(1L, "user1");
        UserEntity userEntity2 = new UserEntity(2L, "user2");
        Flashcard flashcard = new Flashcard(1L, "english word", null, null);
        Flashcard flashcard2= new Flashcard(2L, "amazing", null, null);
        Translation translation = new Translation(1L,flashcard,"deutsches Wort");
        Translation translation2 = new Translation(2L,flashcard,"erstaunlich");
        flashcard.setTranslations(List.of(translation));
        flashcard2.setTranslations(List.of(translation2));
        Duel duel = new Duel(1L, List.of(userEntity, userEntity2), List.of(flashcard,flashcard2));

        assertThat(duel.getPlayers()).hasSize(2).extracting("username").contains("user1","user2");

        assertThat(service.joinDuel(duel.getDuelId(), userEntity2.getUserId())).isFalse();

        Duel currentDuel = service.getById(duel.getDuelId());
        assertThat(currentDuel).isNotNull();
        assertThat(currentDuel.getPlayers()).hasSize(2).extracting("username").contains("user1", "user2");

    }
}
