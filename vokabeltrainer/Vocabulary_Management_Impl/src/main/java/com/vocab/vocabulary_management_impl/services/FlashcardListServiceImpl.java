package com.vocab.vocabulary_management_impl.services;

import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management.entities.Translation;
import com.vocab.vocabulary_management.repos.FlashcardListRepo;
import com.vocab.vocabulary_management.repos.FlashcardRepo;


import com.vocab.vocabulary_management.repos.TranslationRepo;
import com.vocab.vocabulary_management.services.FlashcardListService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FlashcardListServiceImpl implements FlashcardListService {
    @Autowired
    private FlashcardRepo flashcardRepo;
    @Autowired
    private TranslationRepo translationRepo;
    @Autowired
    private FlashcardListRepo flashcardListRepo;
    private final String default_path = "/vocabFiles/";
    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean createFlashcardList(String filename) {
        return null;
    }
    @PostConstruct
    public void readAndSaveFlashcardListFromTxtFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\mseet\\IdeaProjects\\KBAckend\\vokabeltrainer\\Vocabulary_Management_API\\src\\main\\java\\com\\vocab\\vocabulary_management\\entities\\vocab.txt"))) {
            String line;
            FlashcardList flashcardList = new FlashcardList();
            line = reader.readLine();
            Pattern metadataPattern = Pattern.compile("\\{\\{\\{(.*?)\\}\\}\\}");
            Matcher metadataMatcher = metadataPattern.matcher(line);
            if(metadataMatcher.find()) {
                flashcardList.setCategory(metadataMatcher.group().replace("{" , "").replace("}" , ""));
            }
            if(metadataMatcher.find()) {
                flashcardList.setOriginalLanguage(metadataMatcher.group().replace("{" , "").replace("}" , ""));
            }
            if(metadataMatcher.find()) {
                flashcardList.setTranslationLanguage(metadataMatcher.group().replace("{" , "").replace("}" , ""));
            }
            flashcardListRepo.save(flashcardList);
            while((line = reader.readLine()) != null) {
                var list = separateString(line);
                Flashcard flashcard = new Flashcard();
                flashcard.setOriginalText(list.get(0));
                flashcard.setFlashcardList(flashcardList);
                flashcardRepo.save(flashcard);
                for (int i = 1; i < list.size(); i++) {
                    Translation translation = new Translation();
                    translation.setTranslationText(list.get(i));
                    translation.setFlashcard(flashcard);
                    translationRepo.save(translation);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<String> separateString(String input) {
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
    public FlashcardList getById(Long id) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FlashcardList> getAll() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Flashcard> getFlashcardsByFlashcardListId(Long id) {
        return null;
    }
}