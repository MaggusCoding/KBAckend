package com.vocabduel.vocabulary_duel_impl;

import com.management.user_management.entities.User;
import com.vocab.vocabulary_duel.entities.Duel;
import com.vocab.vocabulary_duel.entities.Round;
import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.Translation;
import com.vocabduel.vocabulary_duel_impl.services.RoundServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RoundServiceImplTest {

    RoundServiceImpl service = new RoundServiceImpl();

    @Test
    void testCreateRoundExpectOk(){
        User user = new User(1L, "user1");
        Flashcard flashcard = new Flashcard(1L, "english word", null, null);
        Translation translation = new Translation(1L,flashcard,"deutsches Wort");
        flashcard.setTranslations(List.of(translation));
        Duel duel = new Duel(1L, List.of(user), List.of(flashcard));
        Round round = new Round(1L, duel, flashcard, null, List.of("parola italiana", "mot français", "palavra portuguesa"));

        service.createRound(round);

        Round round1 = service.getById(1L);
        assertThat(round1).isNotNull();
        assertThat(round1.getQuestionedFlashcard()).usingRecursiveComparison().isEqualTo(flashcard);
        assertThat(round1.getWrongAnswers()).hasSize(3).containsAll(List.of("parola italiana", "mot français", "palavra portuguesa"));
        assertThat(round1.getDuel()).usingRecursiveComparison().isEqualTo(duel);

    }

    @Test
    void testGenerateWrongAnswersExpectOk(){
        User user = new User(1L, "user1");
        Flashcard flashcard = new Flashcard(1L, "english word", null, null);
        Translation translation = new Translation(1L,flashcard,"deutsches Wort");
        flashcard.setTranslations(List.of(translation));
        Duel duel = new Duel(1L, List.of(user), List.of(flashcard));
        Round round = new Round(1L, duel, flashcard, null, null);

        List<String> wrongAnswers = service.generateWrongAnswers(round);

        assertThat(wrongAnswers).isNotNull().hasSize(3).doesNotContain("deutsches Wort");
    }

    @Test
    void testGetAllRoundsByDuelExpectOk(){
        User user = new User(1L, "user1");
        User user2 = new User(2L, "user2");
        Flashcard flashcard = new Flashcard(1L, "english word", null, null);
        Flashcard flashcard2= new Flashcard(2L, "amazing", null, null);
        Translation translation = new Translation(1L,flashcard,"deutsches Wort");
        Translation translation2 = new Translation(2L,flashcard,"erstaunlich");
        flashcard.setTranslations(List.of(translation));
        flashcard2.setTranslations(List.of(translation2));
        Duel duel = new Duel(1L, List.of(user,user2), List.of(flashcard,flashcard2));
        Round round = new Round(1L, duel, flashcard, null, List.of("parola italiana", "mot français", "palavra portuguesa"));
        Round round2 = new Round(2L, duel, flashcard, null, List.of("mies", "wasser", "amazon"));
        service.createRound(round);
        service.createRound(round2);

        List<Round> rounds = service.getAllRoundsByDuel(1L);

        assertThat(rounds).isNotNull().isNotEmpty().hasSize(2);
        assertThat(rounds.get(0)).usingRecursiveComparison().isEqualTo(round);
        assertThat(rounds.get(1)).usingRecursiveComparison().isEqualTo(round2);

    }
}
