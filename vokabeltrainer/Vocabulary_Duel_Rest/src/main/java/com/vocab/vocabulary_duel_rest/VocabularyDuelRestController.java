package com.vocab.vocabulary_duel_rest;

import com.vocab.user_management.entities.UserEntity;
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
    @GetMapping("/api/duel/winners/{duelID}")
    public ResponseEntity<List<UserEntity>> getWinners(@PathVariable Long duelID){
        try {
            List<UserEntity> userEntities = duelService.calculateWinner(duelID);
            return ResponseEntity.ok(userEntities);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("api/duel/{duelID}")
    public ResponseEntity<DuelDTO> getDuelById(@PathVariable Long duelID){
        Duel duel;
        DuelDTO duelDTO = new DuelDTO();
        try{
            if(duelService.getById(duelID).isPresent()){
                duel = duelService.getById(duelID).get();
                duelDTO = DuelDTO.fromEntity(duel);
            }
            return ResponseEntity.ok(duelDTO);
        } catch (Exception e){
           return ResponseEntity.internalServerError().build();
        }
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
    @GetMapping("/api/duel/tojoin/{userID}")
    public ResponseEntity<List<Duel>> getDuelsToJoin(@PathVariable Long userID){
        try{
           return ResponseEntity.ok(duelService.duelsToJoin(userID));
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("api/duel/tostart/{userID}")
    public ResponseEntity<List<Duel>> getDuelsToStart(@PathVariable Long userID){
        try{
            return ResponseEntity.ok(duelService.duelsToStart(userID));
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("api/duel/start/{duelID}")
    public ResponseEntity<Void> startDuel(@PathVariable Long duelID){
        try{
            duelService.startDuel(duelID);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            ResponseEntity.notFound().build();
        }
        return null;
    }
}
