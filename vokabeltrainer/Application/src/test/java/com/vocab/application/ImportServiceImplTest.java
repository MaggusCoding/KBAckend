package com.vocab.application;

import com.vocab.application.serviceImpl.ImportServiceImpl;
import com.vocab.vocabulary_management.services.FlashcardListService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ImportServiceImplTest {

    private final String FILE_PATH = Paths.get("").toAbsolutePath().toString() +File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"body.txt";
    private final String EMPTY_FILE_PATH = Paths.get("").toAbsolutePath().toString() +File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"vocabFiles2"+File.separator+ "emptyVocabList.txt";
    private static final String DEFAULT_PATH_TEST = Paths.get("").toAbsolutePath().toString() +File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"vocabFiles"+File.separator;

    @InjectMocks
    ImportServiceImpl importService;

    @Mock
    FlashcardListService flashcardListService;

    @Mock
    Paths path;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        //Delete all unzipped files!
        Files.walk(Paths.get(DEFAULT_PATH_TEST))
                .sorted(Comparator.reverseOrder())
                .filter(file -> !file.getFileName().toString().endsWith(".zip") && !file.getFileName().toString().endsWith("vocabFiles"))
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    public void testImportFile() throws IOException {
        when(flashcardListService.createFlashcardList(anyString())).thenReturn(true);

        boolean successImport = importService.importFile(FILE_PATH);

        assertTrue(successImport);
        verify(flashcardListService).createFlashcardList(anyString());

    }

    @Test
    public void testImportInitialFiles() throws IOException {
        when(flashcardListService.createFlashcardList(anyString())).thenReturn(true);
        ReflectionTestUtils.setField(importService, "DEFAULT_PATH", DEFAULT_PATH_TEST);

        boolean importSuccess = importService.importInitialFiles();

        assertTrue(importSuccess);
        verify(flashcardListService, times(16)).createFlashcardList(anyString());
        long amountOfFiles = Files.walk(Paths.get(DEFAULT_PATH_TEST)).filter(file -> !Files.isDirectory(file) && !file.getFileName().toString().endsWith(".zip")).count();
        assertEquals(amountOfFiles, 16);
    }

    @Test
    public void testImportFileExpectFileNotFoundException(){
        assertThrows(FileNotFoundException.class, () -> importService.importFile(""));
    }

    @Test
    public void testImportEmptyFile() throws IOException {
        boolean successImport = importService.importFile(EMPTY_FILE_PATH);

        assertFalse(successImport);
        verify(flashcardListService, times(0)).createFlashcardList(anyString());
    }

    @Test
    public void testImportEmptyInitialFiles() throws IOException {
        String path_to_folder = EMPTY_FILE_PATH.substring(0, EMPTY_FILE_PATH.lastIndexOf(File.separator));
        ReflectionTestUtils.setField(importService, "DEFAULT_PATH", path_to_folder);

        boolean importSuccess = importService.importInitialFiles();

        assertFalse(importSuccess);
        verify(flashcardListService, times(0)).createFlashcardList(anyString());
    }
}
