package com.vocab.vocabulary_duel_impl.services;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management_impl.services.UserServiceImpl;
import com.vocab.vocabulary_duel.entities.Answer;
import com.vocab.vocabulary_duel.entities.Duel;
import com.vocab.vocabulary_duel.entities.Round;
import com.vocab.vocabulary_duel.repositories.AnswerRepo;
import com.vocab.vocabulary_duel.repositories.DuelRepo;
import com.vocab.vocabulary_duel.repositories.RoundRepo;
import com.vocab.vocabulary_duel.services.DuelService;
import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.Translation;
import com.vocab.vocabulary_management.repos.FlashcardRepo;
import com.vocab.vocabulary_management.repos.TranslationRepo;
import com.vocab.vocabulary_management_impl.services.FlashcardListServiceImpl;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private AnswerRepo answerRepo;
    @Autowired
    private FlashcardRepo flashcardRepo;

    /**
     * {@inheritDoc}
     */
    @Override
    public Duel createDuel(Long userId, Long flashcardListId) {
        Duel duel = new Duel();
        duel.setFlashcardsForDuel(flashcardListService.getById(flashcardListId));
        duel.setPlayer(userService.getById(userId));
        duel.setStarted(false);
        duel.setFinished(false);
        duelRepo.save(duel);
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
        if (!duel.isStarted() && !isAlreadyJoined) {
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
    public List<UserEntity> calculateWinner(Long duelId) {
        HashMap<UserEntity, Integer> ranking = new HashMap<>();
        Duel duel = duelRepo.findById(duelId).get();
        // initialize ranking-Map
        duel.getPlayers().forEach(player -> ranking.put(player, 0));
        // collect correct Answer for each player by
        List<Round> allRoundsOfDuel = duel.getRounds();
        allRoundsOfDuel.forEach(round -> {
            List<UserEntity> playerWithCorrectAnswer = round.getSelectedAnswers().stream().filter(Answer::getCorrect).map(Answer::getPlayer).toList();
            playerWithCorrectAnswer.forEach(player -> ranking.computeIfPresent(player, (key, val) -> val + 1));
            });
        // sort Hashmap descending
        List<Map.Entry<UserEntity, Integer>> rankingList = ranking.entrySet().stream().sorted(Map.Entry.<UserEntity, Integer> comparingByValue().reversed()).toList();
        int topScore = rankingList.get(0).getValue();
        return rankingList.stream().filter(entry -> entry.getValue().equals(topScore)).map(Map.Entry::getKey).collect(Collectors.toList());
    }


    public void generateRounds(Long duelId) {
        Random rand = new Random();
        Duel duel = duelRepo.findById(duelId).get();
        List<Flashcard> flashcards = duel.getFlashcardsForDuel().getFlashcards();
        List<Translation> allTranslations = translationRepo.findAll();
        List<String> allTranslationStrings = new ArrayList<>();
        allTranslations.forEach(translation ->
                allTranslationStrings.add(translation.getTranslationText()));
        for (int i = 0; i < 10; i++) {
            Round round = new Round();

            int randomInt = rand.nextInt(flashcards.size());
            Flashcard flashcard = flashcards.get(randomInt);
            List<Translation> translations = flashcard.getTranslations();

            int randomIntTrans = rand.nextInt(translations.size());
            Translation translation = translations.get(randomIntTrans);
            String correctAnswer = translation.getTranslationText();

            List<String> wrongAnswers = generateWrongAnswers(correctAnswer, allTranslationStrings);
            String wrongAnswersString = String.join(";", wrongAnswers);


            round.setDuel(duel);
            round.setQuestionedFlashcard(flashcard);
            round.setWrongAnswers(wrongAnswersString);
            roundRepo.save(round);
            flashcards.remove(randomInt);
        }

    }

    private List<String> generateWrongAnswers(String correctAnswer, List<String> allTranslationStrings) {
        List<String> wrongAnswers = new ArrayList<>();
        LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();

        // A map to store strings and their distances
        TreeMap<Integer, List<String>> distanceMap = new TreeMap<>();

        // Calculate distances and store in the map
        for (String translationString : allTranslationStrings) {
            if (!translationString.equals(correctAnswer)) {
                int distance = levenshteinDistance.apply(correctAnswer, translationString);
                distanceMap.computeIfAbsent(distance, k -> new ArrayList<>()).add(translationString);
            }
        }

        // Remove the closest string from the map and add to wrong answers
        Map.Entry<Integer, List<String>> firstEntry = distanceMap.pollFirstEntry();
        if (firstEntry != null && !firstEntry.getValue().isEmpty()) {
            wrongAnswers.add(firstEntry.getValue().get(0)); // Assuming non-empty list for the smallest distance
        }

        // Add next two closest strings
        for (int i = 0; i < 2 && !distanceMap.isEmpty(); ) {
            Map.Entry<Integer, List<String>> entry = distanceMap.pollFirstEntry();
            if (entry != null) {
                for (String str : entry.getValue()) {
                    if (!wrongAnswers.contains(str)) {
                        wrongAnswers.add(str);
                        if (++i == 2) break; // Break when two strings have been added
                    }
                }
            }
        }

        return wrongAnswers;
    }

    public boolean startDuel(Long duelId) {
        Duel duel = null;
        if (duelRepo.findById(duelId).isPresent()) {
            duel = duelRepo.findById(duelId).get();
        } else return false;
        duel.setStarted(true);
        List<Round> rounds = duel.getRounds();
        Round round = rounds.get(0);
        round.setActiveRound(true);
        roundRepo.save(round);
        duelRepo.save(duel);
        return true;
    }


    public List<String> playRound(Long duelId) {
        Random rand = new Random();
        List<String> roundStrings = new ArrayList<>();
        Duel duel = duelRepo.findById(duelId).get();
        List<Round> rounds = duel.getRounds();
        Round activeRound = new Round();
        for (Round round : rounds) {
            if (round.isActiveRound()) {
                activeRound = round;
            }
        }


        Flashcard flashcard = flashcardRepo.findById(activeRound.getQuestionedFlashcard().getFlashCardId()).get();
        String question = flashcard.getOriginalText();

        List<Translation> translations = flashcard.getTranslations();
        int randomIntTrans = rand.nextInt(translations.size());
        Translation translation = translations.get(randomIntTrans);
        String correctAnswer = translation.getTranslationText();

        String wrongAnswers = activeRound.getWrongAnswers();

        roundStrings.add(question);
        roundStrings.addAll(Arrays.stream(wrongAnswers.split(";")).toList());
        roundStrings.add(rand.nextInt(1, 4), correctAnswer);
        return roundStrings;
    }

    /**
     * {@inheritDoc}
     */
    public void saveSelectedAnswer(String selectedAnswer, Long duelId, Long playerId) {
        Duel duel = duelRepo.findById(duelId).orElseThrow(() -> new RuntimeException("Duel(ID: " + duelId + ") does not exist."));
        Round currentRound = roundRepo.findRoundByDuelAndActiveRoundTrue(duel);
        boolean isCorrect = currentRound.getQuestionedFlashcard().getTranslations().stream().anyMatch(translation -> translation.getTranslationText().equalsIgnoreCase(selectedAnswer));
        Answer answer = new Answer();
        answer.setCorrect(isCorrect);
        answer.setRound(currentRound);
        answer.setPlayer(userService.getById(playerId));
        answerRepo.save(answer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activateNextRound(Long duelId) {
        Duel duel = duelRepo.findById(duelId).get();
        Round currentRound = roundRepo.findRoundByDuelAndActiveRoundTrue(duel);
        currentRound.setActiveRound(false);
        Round nextRound = roundRepo.findFirstByDuelAndSelectedAnswersEmpty(duel);
        // all rounds played?
        if (nextRound != null) {
            nextRound.setActiveRound(true);
            roundRepo.save(currentRound);
            roundRepo.save(nextRound);
        }else{
            duel.setFinished(true);
            duelRepo.save(duel);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Duel> duelsToJoin() {
        return duelRepo.findDuelsByStartedIsFalseAndFinishedIsFalse();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Duel> duelsToStart(Long userId){
        UserEntity user = userService.getById(userId);
        List<Duel> possibleDuels = duelRepo.findDuelsByStartedIsFalseAndFinishedIsFalse();
        return possibleDuels.stream().filter(duel -> duel.getPlayers().contains(user)).collect(Collectors.toList());
    }

}
