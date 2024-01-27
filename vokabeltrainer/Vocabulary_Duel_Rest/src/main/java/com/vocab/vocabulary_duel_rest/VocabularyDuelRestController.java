package com.vocab.vocabulary_duel_rest;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.vocabulary_duel_API.dto.AnswerDTO;
import com.vocab.vocabulary_duel_API.dto.DuelCreateRequest;
import com.vocab.vocabulary_duel_API.dto.DuelDTO;
import com.vocab.vocabulary_duel_API.dto.RoundDTO;
import com.vocab.vocabulary_duel_API.entities.Duel;
import com.vocab.vocabulary_duel_impl.services.DuelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@ComponentScan(basePackages = {"com.vocab"})
@CrossOrigin(origins = "http://localhost:5173") // Frontend-Server-URL

public class VocabularyDuelRestController {
    @Autowired
    DuelServiceImpl duelService;

    @PostMapping("/api/duel")
    public ResponseEntity<DuelDTO> createDuel(@RequestBody DuelCreateRequest request) {
       try {
            Duel duel = duelService.createDuel(request.getUserID(), request.getFlashcardListID());
            duelService.generateRounds(duel.getDuelId());
            Duel duelCreated = duelService.getById(duel.getDuelId()).get();
            return ResponseEntity.status(HttpStatus.CREATED).body(DuelDTO.fromEntity(duelCreated));
        } catch (Exception e){
           return ResponseEntity.notFound().build();
       }
    }

    @GetMapping("/api/duel")
    public ResponseEntity<List<DuelDTO>> getAllDuels() {
        List<Duel> duels = duelService.getAll();
        List<DuelDTO> duelDTOLIst = duels.stream().map(DuelDTO::fromEntity).toList();
        if(duelDTOLIst.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(duelDTOLIst);
    }

    @GetMapping("/api/duel/winners")
    public ResponseEntity<List<UserEntity>> getWinners(@RequestParam Long duelid) {
        try {
            List<UserEntity> userEntities = duelService.calculateWinner(duelid);
            if(userEntities.isEmpty())
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(userEntities);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/duel/byid")
    public ResponseEntity<DuelDTO> getDuelById(@RequestParam Long duelid) {
        try {
            Duel duel = duelService.getById(duelid).get();
            DuelDTO  duelDTO = DuelDTO.fromEntity(duel);
            return ResponseEntity.ok(duelDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/api/duel/join")
    public ResponseEntity<DuelDTO> joinDuel(@RequestParam Long duelid, @RequestParam Long userid) {
        try {
            if(!duelService.joinDuel(duelid, userid))
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            DuelDTO duelDTO = DuelDTO.fromEntity(duelService.getById(duelid).get());
            return ResponseEntity.status(HttpStatus.OK).body(duelDTO);
        } catch (ObjectOptimisticLockingFailureException oe) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Duel-Daten nicht aktuell. Bitte neu laden!");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/duel/tojoin")
    public ResponseEntity<List<DuelDTO>> getDuelsToJoin(@RequestParam Long userid) {
        try {
            List<Duel> duels = duelService.duelsToJoin(userid);
            List<DuelDTO> duelDTOS = duels.stream()
                    .map(DuelDTO::fromEntity)
                    .collect(Collectors.toList());
            if(duelDTOS.isEmpty())
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(duelDTOS);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/duel/tostart")
    public ResponseEntity<List<DuelDTO>> getDuelsToStart(@RequestParam Long userid) {
        try {
            List<Duel> duels = duelService.duelsToStart(userid);
            List<DuelDTO> duelDTOS = duels.stream()
                    .map(DuelDTO::fromEntity)
                    .collect(Collectors.toList());
            if(duelDTOS.isEmpty())
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(duelDTOS);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/api/duel/start")
    public ResponseEntity<DuelDTO> startDuel(@RequestParam Long duelid, @RequestParam Long userid) {
        try {
            if(!duelService.startDuel(duelid, userid))
                return ResponseEntity.notFound().build();
            DuelDTO duel = DuelDTO.fromEntity(duelService.getById(duelid).get());
            if(duel.isStarted())
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            return ResponseEntity.status(HttpStatus.OK).body(duel);
        } catch (ObjectOptimisticLockingFailureException oe) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Duel-Date nicht aktuell. Bitte neu laden!");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/duel/playround")
    public ResponseEntity<RoundDTO> playRound(@RequestParam Long duelid) {
        try {
            List<String> list = duelService.playRound(duelid);
            RoundDTO roundDTO = RoundDTO.fromList(list);
            return ResponseEntity.ok(roundDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/api/duel/answer")
    public ResponseEntity<Void> playerAnswer(@RequestBody AnswerDTO request) {
        try {
           if(duelService.saveSelectedAnswer(request.getAnswer(), request.getDuelID(), request.getPlayerID())){
               return ResponseEntity.ok().build();
           }else{
               return ResponseEntity.status(HttpStatus.CONFLICT).build();
           }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // TODO: Endpoint deleteDuel
}
