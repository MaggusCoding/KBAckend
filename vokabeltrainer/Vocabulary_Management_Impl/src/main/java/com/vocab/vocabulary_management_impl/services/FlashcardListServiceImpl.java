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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@ComponentScan(basePackages = {"com.vocab"})
public class FlashcardListServiceImpl implements FlashcardListService {
    @Autowired
    private FlashcardRepo flashcardRepo;
    @Autowired
    private TranslationRepo translationRepo;
    @Autowired
    private FlashcardListRepo flashcardListRepo;

    private final String DEFAULT_PATH = Paths.get("").toAbsolutePath().toString() + "\\vocabFiles\\";

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean createFlashcardList(String content) {
        return null;
    }

    @PostConstruct
    public void readAndSaveInitialFlashcardLists() throws IOException {
        Set<String> files = retrieveFilenamesFromFolder();
        for (String filename : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                FlashcardList flashcardList = new FlashcardList();
                line = reader.readLine();
                Pattern metadataPattern = Pattern.compile("\\{\\{\\{(.*?)\\}\\}\\}");
                Matcher metadataMatcher = metadataPattern.matcher(line);
                if (metadataMatcher.find()) {
                    String category = metadataMatcher.group().replace("{", "").replace("}", "");
                    FlashcardList existingFlashcardlist = flashcardListRepo.findByCategory(category);
                    if (existingFlashcardlist == null) {
                        flashcardList.setCategory(category);
                        if (metadataMatcher.find()) {
                            flashcardList.setOriginalLanguage(metadataMatcher.group().replace("{", "").replace("}", ""));
                        }
                        if (metadataMatcher.find()) {
                            flashcardList.setTranslationLanguage(metadataMatcher.group().replace("{", "").replace("}", ""));
                        }
                        flashcardListRepo.save(flashcardList);
                        addFlashcardsToFlashcardlist(flashcardList, reader);
                    } else {
                        addFlashcardsToFlashcardlist(existingFlashcardlist, reader);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void addFlashcardsToFlashcardlist(FlashcardList flashcardList, BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            var list = separateString(line);
            Flashcard flashcard = new Flashcard();
            flashcard.setFlashcardList(flashcardList);
            // Does the flashcard already exists
            if (flashcardRepo.findByOriginalText(list.get(0)) == null) {
                flashcard.setOriginalText(list.get(0));
                flashcardRepo.save(flashcard);
                for (int i = 1; i < list.size(); i++) {
                    Translation translation = new Translation();
                    translation.setTranslationText(list.get(i));
                    translation.setFlashcard(flashcard);
                    translationRepo.save(translation);
                }
            }
        }
    }

    private Set<String> retrieveFilenamesFromFolder() throws IOException {
        // read all filenames in folder
        Set<String> filenames;
        Path default_folder = Paths.get(DEFAULT_PATH);
        try (Stream<Path> stream = Files.list(default_folder)) {
            filenames = stream.filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .map(filename -> DEFAULT_PATH + filename)
                    .collect(Collectors.toSet());
        }

        // check if a zip-File exists and if so extract the files
        filenames.forEach(filename -> {
            if (filename.endsWith(".zip")) {
                try {
                    extractFilesFromZip(filename);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //collect filenames including absolute path
        try (Stream<Path> stream = Files.walk(default_folder)) {
            return stream.filter(file -> !Files.isDirectory(file) && !file.getFileName().toString().endsWith(".zip"))
                    .map(Path::toAbsolutePath)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        }
    }

    private void extractFilesFromZip(String zipFile) throws IOException {
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile), StandardCharsets.ISO_8859_1);
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(new File(DEFAULT_PATH), zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();


        }
        zis.closeEntry();
        zis.close();
    }

    private File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
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
    public FlashcardList getById(Long id) {
      return  flashcardListRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("FlashcardList not found with id: " + id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FlashcardList> getAll() {
        return flashcardListRepo.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Flashcard> getFlashcardsByFlashcardListId(Long id) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteFlashcardList(Long id) {
        if(flashcardListRepo.countDuelByFlashcardList(id) == 0){
            flashcardListRepo.delete(flashcardListRepo.getById(id));
            return true;
        }
        return false;
    }

}