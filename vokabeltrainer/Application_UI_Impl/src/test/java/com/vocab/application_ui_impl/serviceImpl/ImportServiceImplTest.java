package com.vocab.application_ui_impl.serviceImpl;

public class ImportServiceImplTest {
//
//    private final String FILE_PATH = Paths.get("").toAbsolutePath() +File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"body.txt";
//    private final String EMPTY_FILE_PATH = Paths.get("").toAbsolutePath() +File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+ "emptyVocabList.txt";
//
//    @InjectMocks
//    ImportServiceImpl importService;
//
//    @Mock
//    FlashcardListService flashcardListService;
//
//    @BeforeEach
//    public void setup(){
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testImportFile() throws IOException, ContentEmptyException {
//        Mockito.when(flashcardListService.createFlashcardList(ArgumentMatchers.anyString())).thenReturn(true);
//
//        boolean successImport = importService.importFile(FILE_PATH);
//
//        Assertions.assertTrue(successImport);
//        Mockito.verify(flashcardListService).createFlashcardList(ArgumentMatchers.anyString());
//
//    }
//
//    @Test
//    public void testImportInitialFiles() throws IOException, ContentEmptyException {
//        Mockito.when(flashcardListService.createFlashcardList(ArgumentMatchers.anyString())).thenReturn(true);
//
//        boolean importSuccess = importService.importInitialFiles();
//
//        Assertions.assertTrue(importSuccess);
//        Mockito.verify(flashcardListService, Mockito.times(16)).createFlashcardList(ArgumentMatchers.anyString());
//    }
//
//    @Test
//    public void testImportFileExpectFileNotFoundException(){
//        Assertions.assertThrows(FileNotFoundException.class, () -> importService.importFile(""));
//    }
//
//    @Test
//    public void testImportEmptyFile() throws IOException, ContentEmptyException {
//        boolean successImport = importService.importFile(EMPTY_FILE_PATH);
//
//        Assertions.assertFalse(successImport);
//        Mockito.verify(flashcardListService, Mockito.times(0)).createFlashcardList(ArgumentMatchers.anyString());
//    }

}
