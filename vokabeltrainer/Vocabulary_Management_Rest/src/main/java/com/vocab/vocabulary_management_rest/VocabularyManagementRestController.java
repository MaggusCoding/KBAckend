package com.vocab.vocabulary_management_rest;

import com.vocab.vocabulary_management.dto.FlashcardListDTO;
import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management.services.FlashcardListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@ComponentScan(basePackages = {"com.vocab"})
public class VocabularyManagementRestController {
    @Autowired
    FlashcardListService flashcardListService;

    @GetMapping("/api/vocablist")
    public ResponseEntity<List<FlashcardListDTO>> getAllFlashcardlists(){
        try{
            List<FlashcardList> flashcardLists = flashcardListService.getAll();
            List<FlashcardListDTO> flashcardListDTOS = flashcardLists.stream().map(FlashcardListDTO::fromEntity).toList();
          return ResponseEntity.ok(flashcardListDTOS);
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
}
