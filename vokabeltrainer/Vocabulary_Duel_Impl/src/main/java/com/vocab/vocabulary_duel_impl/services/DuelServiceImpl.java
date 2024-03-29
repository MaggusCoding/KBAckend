package com.vocab.vocabulary_duel_impl.services;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management.exceptions.UserNotExistException;
import com.vocab.user_management.services.UserService;
import com.vocab.vocabulary_duel_API.dto.RankingPlayer;
import com.vocab.vocabulary_duel_API.entities.Answer;
import com.vocab.vocabulary_duel_API.entities.Duel;
import com.vocab.vocabulary_duel_API.entities.Round;
import com.vocab.vocabulary_duel_API.exceptions.*;
import com.vocab.vocabulary_duel_API.repositories.AnswerRepo;
import com.vocab.vocabulary_duel_API.repositories.DuelRepo;
import com.vocab.vocabulary_duel_API.repositories.RoundRepo;
import com.vocab.vocabulary_duel_API.services.DuelService;
import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.Translation;
import com.vocab.vocabulary_management.exceptions.FlashcardListNotExistException;
import com.vocab.vocabulary_management.repos.TranslationRepo;
import com.vocab.vocabulary_management.services.FlashcardListService;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/*
todo: Einen check implementieren der überprüft ob der Spieler für diese Runde bereits eine Answer abgegeben hat
 */
@Service
@ComponentScan(basePackages = {"com.vocab"})
public class DuelServiceImpl implements DuelService {

    @Autowired
    private FlashcardListService flashcardListService;
    @Autowired
    private UserService userService;
    @Autowired
    private DuelRepo duelRepo;
    @Autowired
    private TranslationRepo translationRepo;
    @Autowired
    private RoundRepo roundRepo;
    @Autowired
    private AnswerRepo answerRepo;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Duel createDuel(Long userId, Long flashcardListId) throws UserNotExistException, FlashcardListNotExistException {
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
    @Transactional
    public Boolean joinDuel(long duelId, long userId) throws UserNotExistException, DuelNotExistException, DuelAlreadyStartedException, UserAlreadyPartOfDuelException {
        Duel duel = duelRepo.findById(duelId).orElseThrow(() -> new DuelNotExistException("Duel zur id " + duelId + " existiert nicht."));
        UserEntity user = userService.getById(userId);
        boolean isAlreadyJoined = duel.getPlayers().contains(user);
        if(duel.isStarted()){
            throw new DuelAlreadyStartedException("Das Duel mit id " + duelId + " ist bereits gestartet.");
        }
        if(isAlreadyJoined){
            throw new UserAlreadyPartOfDuelException("User mit id " + userId + " ist bereits dem Duel mit id " + duelId + " beigetreten.");
        }
        duel.setPlayer(user);
        duelRepo.save(duel);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Optional<Duel> getById(Long id) {
        return duelRepo.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Duel> getAll() {
        return duelRepo.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<UserEntity> calculateWinner(Long duelId) throws DuelNotExistException {
        List<RankingPlayer> rankingData = duelRepo.getRankingOfDuel(duelId);
        Duel duel = duelRepo.findById(duelId).orElseThrow(() -> new DuelNotExistException("Duel mit id " + duelId + " existiert nicht."));
        if(!duel.getWinner().isEmpty())
            return duel.getWinner();
        if(rankingData.isEmpty()){
            return List.of();
        }
        Long topScore = rankingData.get(0).getAmountCorrectAnswer();
        List<UserEntity> winners = rankingData.stream()
                .filter(data -> data.getAmountCorrectAnswer() == topScore)
                .map(RankingPlayer::getPlayerId)
                .map(playerId -> {
                    try {
                        return userService.getById(playerId);
                    } catch (UserNotExistException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        duel.setWinner(winners);
        duelRepo.save(duel);
        return winners;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void generateRounds(Long duelId) throws DuelNotExistException {
        Random rand = new Random();
        Duel duel = duelRepo.findById(duelId).orElseThrow(() -> new DuelNotExistException("Duel mit id " + duelId + " existiert nicht."));
        List<Flashcard> flashcards = duel.getFlashcardsForDuel().getFlashcards();
        List<Translation> allTranslations = translationRepo.findAll();
        List<String> allTranslationStrings = new ArrayList<>();
        allTranslations.forEach(translation ->
                allTranslationStrings.add(translation.getTranslationText()));
        for (int i = 0; i < 10; i++) {
            Round round = new Round();

            int randomInt = rand.nextInt(0, flashcards.size());
            Flashcard flashcard = flashcards.get(randomInt);
            List<Translation> translations = flashcard.getTranslations();

            int randomIntTrans = rand.nextInt(0, translations.size());
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteDuel(Long duelId) throws DuelNotExistException {
        if(duelRepo.existsById(duelId)){
            duelRepo.deleteById(duelId);
            return true;
        }
        throw new DuelNotExistException("Duel mit id " + duelId + " existiert nicht.");
    }

    /**
     * {@inheritDoc}
     */
    public List<String> generateWrongAnswers(String correctAnswer, List<String> allTranslationStrings) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Round> getNotPlayedRounds(Long duelId, Long userId) throws UserNotExistException, DuelNotExistException {
        Duel duel = duelRepo.findById(duelId).orElseThrow(() -> new DuelNotExistException("Duel zur id " + duelId + " existiert nicht."));
        UserEntity user = userService.getById(userId);
        return roundRepo.findRoundsByDuelAndNotPlayedByUser(duel, user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Duel> duelsToPlay(Long userId) throws UserNotExistException {
        return duelRepo.findDuelsByStartedIsTrueAndFinishedIsFalseAndPlayersContaining(userService.getById(userId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean startDuel(Long duelId, Long userId) throws DuelNotExistException, UserNotExistException, UserNotPartOfDuelException, DuelAlreadyStartedException {
        Duel duel;
        if (duelRepo.findById(duelId).isPresent()) {
            duel = duelRepo.findById(duelId).get();
        } else throw new DuelNotExistException("Duel mit id " + duelId + " existiert nicht.");
        if(!duel.getPlayers().contains(userService.getById(userId))){
            throw new UserNotPartOfDuelException("Der User mit id " + userId + " ist nicht Teil des Duels mit id " + duelId);
        }
        if(duel.isStarted()){
            throw new DuelAlreadyStartedException("Das Duel mit id " + duelId + " ist bereits gestartet.");
        }
        duel.setStarted(true);
        List<Round> rounds = duel.getRounds();
        Round round = rounds.get(0);
        round.setActiveRound(true);
        roundRepo.save(round);
        duelRepo.save(duel);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Duel startDuelRest(Long duelId, Long userId) throws DuelNotExistException, UserNotPartOfDuelException, DuelAlreadyStartedException, UserNotExistException {
        Duel duel;
        if (duelRepo.findById(duelId).isPresent()) {
            duel = duelRepo.findById(duelId).get();
        } else throw new DuelNotExistException("Duel zur id " + duelId + " existiert nicht.");
        if(!duel.getPlayers().contains(userService.getById(userId))){
            throw new UserNotPartOfDuelException("User mit id " + userId + " nimmt nicht am Duel teil.");
        }
        if(duel.isStarted()){
            throw new DuelAlreadyStartedException("Duel hat bereits gestartet.");
        }
        duel.setStarted(true);
        return duelRepo.save(duel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<String> playRound(Long duelId) throws DuelNotExistException {
        Random rand = new Random();
        List<String> roundStrings = new ArrayList<>();
        Duel duel = duelRepo.findById(duelId).orElseThrow(() -> new DuelNotExistException("Duel zur id " + duelId + " existiert nicht."));
        List<Round> rounds = duel.getRounds();
        Round activeRound = new Round();
        for (Round round : rounds) {
            if (round.isActiveRound()) {
                activeRound = round;
            }
        }

        Flashcard flashcard = activeRound.getQuestionedFlashcard();
        String question = flashcard.getOriginalText();

        List<Translation> translations = flashcard.getTranslations();
        int randomIntTrans = rand.nextInt(translations.size());
        Translation translation = translations.get(randomIntTrans);
        String correctAnswer = translation.getTranslationText();

        String wrongAnswers = activeRound.getWrongAnswers();

        roundStrings.add(question);
        roundStrings.addAll(Arrays.stream(wrongAnswers.split(";")).toList());
        roundStrings.add(correctAnswer);
        return roundStrings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<String> playRoundRest(Long roundId) throws RoundNotExistException {
        Round currentRound = roundRepo.findById(roundId).orElseThrow(() -> new RoundNotExistException("Runde mit id " + roundId + " existiert nicht."));

        return extractFlashcard(currentRound);
    }

    private List<String> extractFlashcard(Round currentRound) {
        Random rand = new Random();
        List<String> roundStrings = new ArrayList<>();
        Flashcard flashcard = currentRound.getQuestionedFlashcard();
        String question = flashcard.getOriginalText();

        List<Translation> translations = flashcard.getTranslations();
        int randomIntTrans = rand.nextInt(translations.size());
        Translation translation = translations.get(randomIntTrans);
        String correctAnswer = translation.getTranslationText();

        String wrongAnswers = currentRound.getWrongAnswers();

        roundStrings.add(String.valueOf(currentRound.getRoundId()));
        roundStrings.add(question);
        roundStrings.addAll(Arrays.stream(wrongAnswers.split(";")).toList());
        roundStrings.add(correctAnswer);
        return roundStrings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean saveSelectedAnswer(String selectedAnswer, Long duelId, Long playerId) throws UserNotExistException, DuelNotExistException, UserAlreadyPlayedRoundException {
        Duel duel = duelRepo.findById(duelId).orElseThrow(() -> new DuelNotExistException("Duel zur id " + duelId + " existiert nicht."));
        Round currentRound = roundRepo.findRoundByDuelAndActiveRoundTrue(duel);
        List<Answer> answers = currentRound.getSelectedAnswers();
        List<UserEntity> userAnswers = answers.stream().map(Answer::getPlayer).toList();
        if(userAnswers.contains(userService.getById(playerId))) {
            throw new UserAlreadyPlayedRoundException("User mit id " + playerId + " hat bereits eine Antwort für die Runde mit id " + currentRound.getRoundId() + " gespeichert.");
        }
        boolean isCorrect = currentRound.getQuestionedFlashcard().getTranslations().stream().anyMatch(translation -> translation.getTranslationText().equalsIgnoreCase(selectedAnswer));
        Answer answer = new Answer();
        answer.setCorrect(isCorrect);
        answer.setRound(currentRound);
        answer.setPlayer(userService.getById(playerId));
        answerRepo.save(answer);
        currentRound.setSingleAnswer(answer);
        roundRepo.save(currentRound);
//        if (allPlayersAnswered(currentRound, duel.getPlayers())) {
//            activateNextRound(duelId);
//        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean saveSelectedAnswerRest(String selectedAnswer, Long roundId, Long playerId) throws UserNotExistException, RoundNotExistException, UserAlreadyPlayedRoundException {
        Round currentRound = roundRepo.findById(roundId).orElseThrow(() -> new RoundNotExistException("Runde mit id " + roundId + " existiert nicht."));
        List<Answer> answers = currentRound.getSelectedAnswers();
        List<UserEntity> userAnswers = answers.stream().map(Answer::getPlayer).toList();
        if(userAnswers.contains(userService.getById(playerId))) {
            throw new UserAlreadyPlayedRoundException("User mit id " + playerId + " hat bereits eine Antwort für die Runde mit id " + roundId + " gespeichert.");
        }
        boolean isCorrect = currentRound.getQuestionedFlashcard().getTranslations().stream().anyMatch(translation -> translation.getTranslationText().equalsIgnoreCase(selectedAnswer));
        Answer answer = new Answer();
        answer.setCorrect(isCorrect);
        answer.setRound(currentRound);
        answer.setPlayer(userService.getById(playerId));
        answerRepo.save(answer);
        currentRound.setSingleAnswer(answer);
        roundRepo.save(currentRound);

        Duel duel = currentRound.getDuel();
        if (allPlayersAnsweredAllRounds(duel)) {
            duel.setFinished(true);
            duelRepo.save(duel);
        }

        return true;
    }


    // bleibt erhalten für die Konsolenapplikation
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void activateNextRound(Long duelId) throws DuelNotExistException {
        Duel duel = duelRepo.findById(duelId).orElseThrow(() -> new DuelNotExistException("Duel mit id " + duelId + " existiert nicht."));
        Round currentRound = roundRepo.findRoundByDuelAndActiveRoundTrue(duel);
        currentRound.setActiveRound(false);

        Round nextRound = roundRepo.findFirstByDuelAndSelectedAnswersEmpty(duel);
        if (nextRound != null) {
                nextRound.setActiveRound(true);
                roundRepo.saveAll(List.of(currentRound, nextRound));
        } else {
            duel.setFinished(true);
            duelRepo.save(duel);
        }
    }

//    private boolean allPlayersAnswered(Round activeRound, List<UserEntity> players) {
//        if(activeRound.getSelectedAnswers() == null) return false;
//        List<UserEntity> playersWithAnswer = activeRound.getSelectedAnswers().stream()
//                .map(Answer::getPlayer)
//                .toList();
//
//        return playersWithAnswer.containsAll(players);
//    }

    private boolean allPlayersAnsweredAllRounds(Duel duel) {
        for(Round round : duel.getRounds()){
            List<UserEntity> playersWithAnswer = round.getSelectedAnswers().stream()
                    .map(Answer::getPlayer)
                    .toList();
            if(!playersWithAnswer.containsAll(duel.getPlayers())){
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Duel> duelsToJoin(Long loggedInUser) throws UserNotExistException {
        UserEntity user = userService.getById(loggedInUser);
        List<Duel> possibleDuels =duelRepo.findDuelsByStartedIsFalseAndFinishedIsFalse();
        return possibleDuels.stream().filter(duel -> !duel.getPlayers().contains(user)).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Duel> duelsToStart(Long userId) throws UserNotExistException {
        UserEntity user = userService.getById(userId);
        List<Duel> possibleDuels = duelRepo.findDuelsByStartedIsFalseAndFinishedIsFalse();
        return possibleDuels.stream().filter(duel -> duel.getPlayers().contains(user)).collect(Collectors.toList());
    }

}
