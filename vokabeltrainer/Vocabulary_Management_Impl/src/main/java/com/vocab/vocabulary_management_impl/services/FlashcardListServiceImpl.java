package com.vocab.vocabulary_management_impl.services;

import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management.entities.Translation;
import com.vocab.vocabulary_management.exceptions.ContentEmptyException;
import com.vocab.vocabulary_management.exceptions.FlashcardListNotExistException;
import com.vocab.vocabulary_management.exceptions.FlashcardListStillInUseException;
import com.vocab.vocabulary_management.repos.FlashcardListRepo;
import com.vocab.vocabulary_management.repos.FlashcardRepo;
import com.vocab.vocabulary_management.repos.TranslationRepo;
import com.vocab.vocabulary_management.services.FlashcardListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@ComponentScan(basePackages = {"com.vocab"})
public class FlashcardListServiceImpl implements FlashcardListService {
    @Autowired
    private FlashcardRepo flashcardRepo;
    @Autowired
    private TranslationRepo translationRepo;
    @Autowired
    private FlashcardListRepo flashcardListRepo;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Boolean createFlashcardList(String content) throws ContentEmptyException {
        List<String> lines = List.of(content.split(System.lineSeparator()));
        if(lines.isEmpty() || lines.get(0).trim().isEmpty()){
            throw new ContentEmptyException("Der Inhalt der Vokabelliste darf nicht leer sein.");
        }
        FlashcardList flashcardList = new FlashcardList();
        FlashcardList existingFlashcardlist = null;
        Pattern metadataPattern = Pattern.compile("\\{\\{\\{(.*?)\\}\\}\\}");
        Matcher metadataMatcher = metadataPattern.matcher(lines.get(0));
        if (metadataMatcher.find()) {
            String category = metadataMatcher.group().replace("{", "").replace("}", "");
            existingFlashcardlist = flashcardListRepo.findByCategory(category);
            if (existingFlashcardlist == null) {
                flashcardList.setCategory(category);
                if (metadataMatcher.find()) {
                    flashcardList.setOriginalLanguage(metadataMatcher.group().replace("{", "").replace("}", ""));
                }
                if (metadataMatcher.find()) {
                    flashcardList.setTranslationLanguage(metadataMatcher.group().replace("{", "").replace("}", ""));
                }
                flashcardListRepo.save(flashcardList);
            }
        }

        for (int x = 1; x < lines.size(); x++) {
            String line = lines.get(x);
            if (existingFlashcardlist == null) {
                addFlashcardsToFlashcardlist(flashcardList, line);
            } else {
                addFlashcardsToFlashcardlist(existingFlashcardlist, line);
            }
        }
        return true;
    }

    private void addFlashcardsToFlashcardlist(FlashcardList flashcardList, String line) {

        var list = separateString(line);
        Flashcard flashcard = new Flashcard();
        flashcard.setFlashcardList(flashcardList);
        // Does the flashcard already exists
        if (!flashcardRepo.existsByOriginalText(list.get(0).toString())) {
            flashcard.setOriginalText(list.get(0).toString());
            flashcardRepo.save(flashcard);
            for (int i = 1; i < list.size(); i++) {
                Translation translation = new Translation();
                translation.setTranslationText(list.get(i).toString());
                translation.setFlashcard(flashcard);
                translationRepo.save(translation);
            }
        }
    }

    private static List<String> separateString(String input) {
        List<String> arrays = new ArrayList<>();

        // Define the regular expression pattern to match words inside curly braces
        Pattern pattern = Pattern.compile("\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(input);

        // Find and add all matching substrings to the arrays list
        while (matcher.find()) {
            arrays.add(matcher.group(1));
        }

        return arrays;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public FlashcardList getById(Long flashcardListId) throws FlashcardListNotExistException {
        return flashcardListRepo.findById(flashcardListId)
                .orElseThrow(() -> new FlashcardListNotExistException("FlashcardList mit id " + flashcardListId + " existiert nicht."));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<FlashcardList> getAll() {
        return flashcardListRepo.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean deleteFlashcardList(Long flashcardListId) throws FlashcardListNotExistException, FlashcardListStillInUseException {
        if (!flashcardListRepo.existsById(flashcardListId)){
            throw new FlashcardListNotExistException("FlashcardList mit id " + flashcardListId + " existiert nicht.");
        }
        if(flashcardListRepo.countDuelByFlashcardList(flashcardListId) > 0) {
            throw new FlashcardListStillInUseException("FlashcardList mit id " + flashcardListId + " wird noch in einem Duell verwendet. Bitte beenden Sie das Duell oder spielen sie es zu Ende.");
        }
        flashcardListRepo.deleteById(flashcardListId);
        return true;
    }

}