package com.vocab.vocabulary_duel_impl.services;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management_impl.services.UserServiceImpl;
import com.vocab.vocabulary_duel.entities.Duel;
import com.vocab.vocabulary_duel.entities.Round;
import com.vocab.vocabulary_duel.repositories.DuelRepo;
import com.vocab.vocabulary_duel.repositories.RoundRepo;
import com.vocab.vocabulary_duel.services.DuelService;
import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management.entities.Translation;
import com.vocab.vocabulary_management.repos.TranslationRepo;
import com.vocab.vocabulary_management_impl.services.FlashcardListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.apache.commons.text.similarity.LevenshteinDistance;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@ComponentScan(basePackages = {"com.vocab"})
public class DuelServiceImpl implements DuelService {

    @Autowired
    private FlashcardListServiceImpl flashcardListService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private DuelRepo duelRepo;
    @Autowired
    private TranslationRepo translationRepo;
    @Autowired
    private RoundRepo roundRepo;
    /**
     * {@inheritDoc}
     */
    @Override
    public Duel createDuel(Long userId, Long flashcardListId) {
        Duel duel = new Duel();
        duel.setFlashcardsForDuel(flashcardListService.getById(flashcardListId));
        duel.setPlayer(userService.getById(userId));
        duel.setStarted(false);
        duelRepo.save(duel);
        duelRepo.flush();
        return duel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean joinDuel(long duelId, long userId) {
        Duel duel = duelRepo.findById(duelId).orElseThrow();
        UserEntity user = userService.getById(userId);
        boolean isAlreadyJoined = duelRepo.findById(duelId).get().getPlayers().contains(user);
        if(!duel.isStarted()&&!isAlreadyJoined) {
            duel.setPlayer(userService.getById(userId));
            duelRepo.save(duel);
            return true;
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Duel> getById(Long id) {
        return duelRepo.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Duel> getAll() {
        return duelRepo.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserEntity> calculateWinner(Duel duel) {
        return null;
        //gt
    }


    public void generateRounds(Long duelId) {
        Random rand = new Random();
        Duel duel = duelRepo.findById(duelId).get();
        List<Flashcard> flashcards = duel.getFlashcardsForDuel().getFlashcards();
        List<Translation> allTranslations = translationRepo.findAll();
        List<String> allTranslationStrings = new ArrayList<>();
        allTranslations.forEach(translation ->
                allTranslationStrings.add(translation.getTranslationText()));
        for(int i=0; i<3;i++){
            Round round = new Round();

            int randomInt = rand.nextInt(flashcards.size());
            Flashcard flashcard = flashcards.get(randomInt);
            List<Translation> translations = flashcard.getTranslations();

            int randomIntTrans = rand.nextInt(translations.size());
            Translation translation = translations.get(randomIntTrans);
            String correctAnswer = translation.getTranslationText();

            List<String> wrongAnswers = generateWrongAnswers(correctAnswer, allTranslationStrings);
            String wrongAnswersString = String.join("/",wrongAnswers);
            // Use correctAnswer and wrongAnswers as needed

            round.setDuel(duel);
            round.setQuestionedFlashcard(flashcard);
            round.setWrongAnswers(wrongAnswersString);
            roundRepo.save(round);
            flashcards.remove(randomInt);
        }

    }
    private List<String> generateWrongAnswers(String correctAnswer, List<String> allTranslationStrings) {
        List<String> wrongAnswers = new ArrayList<>();

        // You can use LevenshteinDistance to find the closest string
        LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();

        // Find the closest string (excluding the correct answer)
        int minDistance = Integer.MAX_VALUE;
        String closestString = "";
        for (String translationString : allTranslationStrings) {
            if (!translationString.equals(correctAnswer)) {
                int distance = levenshteinDistance.apply(correctAnswer, translationString);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestString = translationString;
                }
            }
        }

        // Add the closest string as a wrong answer
        wrongAnswers.add(closestString);

        // Add two more distinct wrong answers (excluding the correct answer and the closest string)
        for (int i = 0; i < 2; i++) {
            String randomWrongAnswer;
            do {
                randomWrongAnswer = allTranslationStrings.get(new Random().nextInt(allTranslationStrings.size()));
            } while (randomWrongAnswer.equals(correctAnswer) || randomWrongAnswer.equals(closestString) || wrongAnswers.contains(randomWrongAnswer));
            wrongAnswers.add(randomWrongAnswer);
        }

        return wrongAnswers;
    }
    public boolean startDuel(Long duelId){
        Duel duel = null;
       if(duelRepo.findById(duelId).isPresent()) {
         duel = duelRepo.findById(duelId).get();
       }else return false;
       duel.setStarted(true);

       return true;
    }
}
