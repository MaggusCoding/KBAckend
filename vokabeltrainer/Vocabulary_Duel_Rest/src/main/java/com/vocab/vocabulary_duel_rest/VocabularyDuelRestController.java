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
    public ResponseEntity<DuelDTO> createDuel(@RequestBody DuelCreateRequest request) {
       try {
            Duel duel = duelService.createDuel(request.getUserID(), request.getFlashcardListID());
            duelService.generateRounds(duel.getDuelId());
            Duel duelCreated = duelService.getById(duel.getDuelId()).get();
            return ResponseEntity.status(HttpStatus.CREATED).body(DuelDTO.fromEntity(duelCreated));
        }catch (Exception e){
           return ResponseEntity.notFound().build();
       }
    }

    @GetMapping("/api/duel")
    public ResponseEntity<List<DuelDTO>> getAllDuels() {
        List<Duel> duels = duelService.getAll();
        List<DuelDTO> duelDTOLIst = duels.stream().map(DuelDTO::fromEntity).toList();
        return ResponseEntity.ok(duelDTOLIst);
    }

    @GetMapping("/api/duel/winners")
    public ResponseEntity<List<UserEntity>> getWinners(@RequestParam Long duelID) {
        try {
            List<UserEntity> userEntities = duelService.calculateWinner(duelID);
            return ResponseEntity.ok(userEntities);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/api/duel/byid")
    public ResponseEntity<DuelDTO> getDuelById(@RequestParam Long duelid) {
        try {
            Duel duel = duelService.getById(duelid).get();
            DuelDTO  duelDTO = DuelDTO.fromEntity(duel);
            return ResponseEntity.ok(duelDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/api/duel/join")
    public ResponseEntity<Void> joinDuel(@RequestParam Long duelid, @RequestParam Long userid) {
        try {
            duelService.joinDuel(duelid, userid);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/api/duel/tojoin")
    public ResponseEntity<List<DuelDTO>> getDuelsToJoin(@RequestParam Long userID) {
        try {
            List<Duel> duels = duelService.duelsToJoin(userID);
            List<DuelDTO> duelDTOS = duels.stream()
                    .map(DuelDTO::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(duelDTOS);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/duel/tostart")
    public ResponseEntity<List<DuelDTO>> getDuelsToStart(@RequestParam Long userID) {
        try {
            List<Duel> duels = duelService.duelsToStart(userID);
            List<DuelDTO> duelDTOS = duels.stream()
                    .map(DuelDTO::fromEntity)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(duelDTOS);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/api/duel/start")
    public ResponseEntity<Void> startDuel(@RequestParam Long duelID) {
        try {
            duelService.startDuel(duelID);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/duel/playround")
    public ResponseEntity<RoundDTO> playRound(@RequestParam Long duelID) {
        try {
            List<String> list = duelService.playRound(duelID);
            RoundDTO roundDTO = RoundDTO.fromList(list);
            return ResponseEntity.ok(roundDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/api/duel/answer")
    public ResponseEntity<Void> playerAnswer(@RequestBody AnswerDTO request) {
        try {
            duelService.saveSelectedAnswer(request.getAnswer(), request.getDuelID(), request.getPlayerID());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
