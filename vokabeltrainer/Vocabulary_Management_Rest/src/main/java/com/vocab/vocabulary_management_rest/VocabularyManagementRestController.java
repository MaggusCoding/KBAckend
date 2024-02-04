package com.vocab.vocabulary_management_rest;

import com.vocab.vocabulary_management.dto.FlashcardListDTO;
import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management.exceptions.ContentEmptyException;
import com.vocab.vocabulary_management.exceptions.FlashcardListNotExistException;
import com.vocab.vocabulary_management.exceptions.FlashcardListStillInUseException;
import com.vocab.vocabulary_management.services.FlashcardListService;
import com.vocab.vocabulary_management_impl.services.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@ComponentScan(basePackages = {"com.vocab"})
@CrossOrigin(origins = "http://localhost:5173") // Frontend-Server-URL
public class VocabularyManagementRestController {
    @Autowired
    FlashcardListService flashcardListService;
    @Autowired
    ImportService importService;

    @PostMapping("api/vocablist/createinitial")
    public ResponseEntity<FlashcardListDTO> createInitialFlashcardlist() throws IOException, ContentEmptyException {
        importService.importInitialFiles();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/vocablist/byid")
    public ResponseEntity<FlashcardListDTO> getById(@RequestParam Long flashcardListId) throws FlashcardListNotExistException {
        FlashcardList flashcardList = flashcardListService.getById(flashcardListId);
        return ResponseEntity.ok(FlashcardListDTO.fromEntity(flashcardList));
    }

    @GetMapping("/api/vocablist")
    public ResponseEntity<List<FlashcardListDTO>> getAllFlashcardlists() {
        List<FlashcardList> flashcardLists = flashcardListService.getAll();
        List<FlashcardListDTO> flashcardListDTOS = flashcardLists.stream().map(FlashcardListDTO::fromEntity).toList();
        return ResponseEntity.ok(flashcardListDTOS);
    }

    @PostMapping("/api/vocablist")
    public ResponseEntity<FlashcardListDTO> createFlashcardList(@RequestBody FlashcardListDTO request) throws ContentEmptyException {
        flashcardListService.createFlashcardList(request.getContent());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/vocablist")
    public ResponseEntity<FlashcardListDTO> deleteFlashcardList(@RequestParam Long flashcardListId) throws FlashcardListNotExistException, FlashcardListStillInUseException {
        flashcardListService.deleteFlashcardList(flashcardListId);
        return ResponseEntity.ok().build();
    }
}
