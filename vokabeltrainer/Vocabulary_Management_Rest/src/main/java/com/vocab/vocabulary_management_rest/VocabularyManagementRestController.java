package com.vocab.vocabulary_management_rest;

import com.vocab.vocabulary_management.dto.FlashcardListDTO;
import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management.services.FlashcardListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ComponentScan(basePackages = {"com.vocab"})
@CrossOrigin(origins = "http://localhost:5173") // Frontend-Server-URL
public class VocabularyManagementRestController {
    @Autowired
    FlashcardListService flashcardListService;

    @GetMapping("/api/vocablist/byid")
    public ResponseEntity<FlashcardListDTO> getById(@RequestParam Long flashcardListId){
        try{
            FlashcardList flashcardList = flashcardListService.getById(flashcardListId);
            return ResponseEntity.ok(FlashcardListDTO.fromEntity(flashcardList));
        } catch (Exception e){
            FlashcardListDTO error = new FlashcardListDTO();
            error.setErrorMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/api/vocablist")
    public ResponseEntity<List<FlashcardListDTO>> getAllFlashcardlists(){
        try{
            List<FlashcardList> flashcardLists = flashcardListService.getAll();
            List<FlashcardListDTO> flashcardListDTOS = flashcardLists.stream().map(FlashcardListDTO::fromEntity).toList();
          return ResponseEntity.ok(flashcardListDTOS);
        } catch (Exception e){
            FlashcardListDTO error = new FlashcardListDTO();
            error.setErrorMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(List.of(error));
        }
    }

    @PostMapping("/api/vocablist")
    public ResponseEntity<FlashcardListDTO> createFlashcardList(@RequestBody FlashcardListDTO request){
        try{
            if(flashcardListService.createFlashcardList(request.getContent())) {
                return ResponseEntity.ok().build();
            }else{
                FlashcardListDTO error = new FlashcardListDTO();
                error.setErrorMessage("Fehler beim Speichern der FlashcardList.");
                return ResponseEntity.internalServerError().body(error);
            }
        } catch(Exception e){
            FlashcardListDTO error = new FlashcardListDTO();
            error.setErrorMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @DeleteMapping("/api/vocablist")
    public ResponseEntity<FlashcardListDTO> deleteFlashcardList(@RequestParam Long flashcardListId){
        try{
            if(flashcardListService.deleteFlashcardList(flashcardListId)) {
                return ResponseEntity.ok().build();
            }else{
                FlashcardListDTO error = new FlashcardListDTO();
                error.setErrorMessage("FlashcardList mit id " + flashcardListId + " existiert nicht oder wird noch in einem Duel gespielt. Bitte das Duel vorher löschen.");
                return ResponseEntity.internalServerError().body(error);
            }
        } catch(Exception e){
            FlashcardListDTO error = new FlashcardListDTO();
            error.setErrorMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
