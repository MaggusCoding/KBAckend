package com.vocab.vocabulary_duel_rest;

import com.vocab.vocabulary_duel_API.dto.DuelCreateRequest;
import com.vocab.vocabulary_duel_API.dto.DuelDTO;
import com.vocab.vocabulary_duel_API.entities.Duel;
import com.vocab.vocabulary_duel_impl.services.DuelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@ComponentScan(basePackages = {"com.vocab"})
public class VocabularyDuelRestController {
    @Autowired
    DuelServiceImpl duelService;

    @PostMapping("/api/duel")
    public ResponseEntity<Void> createDuel(@RequestBody DuelCreateRequest request) throws URISyntaxException {
        try {
            Duel duel = duelService.createDuel(request.getUserID(), request.getFlashcardListID());
            duelService.generateRounds(duel.getDuelId());
            URI uri = new URI("/api/duel/" + duel.getDuelId());
            return ResponseEntity.created(uri).build();
        }catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/api/duel")
    public ResponseEntity<List<DuelDTO>> getAllDuels(){
            List<Duel> duels = duelService.getAll();
            List<DuelDTO> duelDTOLIst = duels.stream().map(DuelDTO::fromEntity).toList();
            return ResponseEntity.ok(duelDTOLIst);
    }
    @PutMapping("/api/duel/{duelID}/{userID}")
    public ResponseEntity<Void> joinDuel(@PathVariable Long duelID, @PathVariable Long userID){
        try {
            duelService.joinDuel(duelID, userID);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
}
