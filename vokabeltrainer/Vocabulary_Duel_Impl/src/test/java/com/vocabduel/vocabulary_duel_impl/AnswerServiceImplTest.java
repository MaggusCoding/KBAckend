package com.vocabduel.vocabulary_duel_impl;

import com.management.user_management.entities.User;
import com.vocab.vocabulary_duel.entities.Answer;
import com.vocab.vocabulary_duel.entities.Round;
import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.Translation;
import com.vocabduel.vocabulary_duel_impl.services.AnswerServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class AnswerServiceImplTest {

    AnswerServiceImpl service = new AnswerServiceImpl();
    @Test
    void testCreateAnswerExpectOk() {
        User user = new User(1L, "user1");
        Flashcard flashcard = new Flashcard(1L, "english word", null, null);
        Translation translation = new Translation(1L,flashcard,"deutsches Wort");
        flashcard.setTranslations(List.of(translation));
        Round round = new Round(1L, null, flashcard, null, null);
        Answer answer = new Answer(1L, user, flashcard, round, "deutsches Wort");

        service.createAnswer(answer);

        Answer answer1 = service.getById(1L);
        assertThat(answer1).isNotNull();
        assertThat(answer1.isCorrect()).isTrue();
        assertThat(answer1.getRound().getSelectedAnswers()).hasSize(1);
        assertThat(answer1.getRound().getSelectedAnswers().get(0)).usingRecursiveComparison().isEqualTo(answer);
        assertThat(answer1.isCorrect()).isTrue();
    }

    @Test
    void testGetAllAnswersForPlayedRoundExpectOk(){
        User user = new User(1L, "user1");
        Flashcard flashcard = new Flashcard(1L, "english word", null, null);
        Translation translation = new Translation(1L,flashcard,"deutsches Wort");
        flashcard.setTranslations(List.of(translation));
        Round round = new Round(1L, null, flashcard, null, null);
        Answer answer = new Answer(1L, user, flashcard, round, "deutsches Wort");
        service.createAnswer(answer);

        List<Answer> allAnswers = service.getAllAnswersByRound(1L);

        assertThat(allAnswers).isNotNull().hasSize(1);
        assertThat(allAnswers.get(0)).usingRecursiveComparison().isEqualTo(answer);

    }
    @Test
    void testGetAllAnswersForNotPlayedRoundExpectEmptyResult(){
        User user = new User(1L, "user1");
        Flashcard flashcard = new Flashcard(1L, "english word", null, null);
        Translation translation = new Translation(1L,flashcard,"deutsches Wort");
        flashcard.setTranslations(List.of(translation));
        Round round = new Round(1L, null, flashcard, null, null);

        List<Answer> allAnswers = service.getAllAnswersByRound(1L);

        assertThat(allAnswers).isNotNull().isEmpty();

    }

}
