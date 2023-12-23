package com.vocab.application;

import com.vocab.application.serviceImpl.ImportServiceImpl;
import com.vocab.vocabulary_management.services.FlashcardListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ImportServiceImplTest {

    private final String FILE_PATH = Paths.get("").toAbsolutePath() +File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"body.txt";
    private final String EMPTY_FILE_PATH = Paths.get("").toAbsolutePath() +File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+ "emptyVocabList.txt";

    @InjectMocks
    ImportServiceImpl importService;

    @Mock
    FlashcardListService flashcardListService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
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

        boolean importSuccess = importService.importInitialFiles();

        assertTrue(importSuccess);
        verify(flashcardListService, times(16)).createFlashcardList(anyString());
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

}
