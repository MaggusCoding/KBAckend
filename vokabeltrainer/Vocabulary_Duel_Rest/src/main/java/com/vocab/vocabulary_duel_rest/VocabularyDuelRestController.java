package com.vocab.vocabulary_duel_rest;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.vocabulary_duel_API.dto.AnswerDTO;
import com.vocab.vocabulary_duel_API.dto.DuelCreateRequest;
import com.vocab.vocabulary_duel_API.dto.DuelDTO;
import com.vocab.vocabulary_duel_API.dto.RoundDTO;
import com.vocab.vocabulary_duel_API.entities.Duel;
import com.vocab.vocabulary_duel_API.entities.Round;
import com.vocab.vocabulary_duel_API.services.DuelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@ComponentScan(basePackages = {"com.vocab"})
@CrossOrigin(origins = "http://localhost:5173") // Frontend-Server-URL

public class VocabularyDuelRestController {
    @Autowired
    DuelService duelService;

    @PostMapping("/api/duel")
    public ResponseEntity<DuelDTO> createDuel(@RequestBody DuelCreateRequest request) {
        try {
            Duel duel = duelService.createDuel(request.getUserId(), request.getFlashcardListId());
            duelService.generateRounds(duel.getDuelId());
            Duel duelCreated = duelService.getById(duel.getDuelId()).get();
            return ResponseEntity.status(HttpStatus.CREATED).body(DuelDTO.fromEntity(duelCreated));
        } catch (Exception e) {
            DuelDTO error = new DuelDTO();
            error.setErrorMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/api/duel")
    public ResponseEntity<List<DuelDTO>> getAllDuels() {
        List<Duel> duels = duelService.getAll();
        List<DuelDTO> duelDtoList = duels.stream().map(DuelDTO::fromEntity).toList();
        if (duelDtoList.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(duelDtoList);
    }

    @GetMapping("/api/duel/winners")
    public ResponseEntity<List<UserEntity>> getWinners(@RequestParam Long duelId) {
        try {
            List<UserEntity> userEntities = duelService.calculateWinner(duelId);
            return ResponseEntity.ok(userEntities);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/api/duel/byid")
    public ResponseEntity<DuelDTO> getDuelById(@RequestParam Long duelId) {
        try {
            Duel duel = duelService.getById(duelId).get();
            DuelDTO duelDTO = DuelDTO.fromEntity(duel);
            return ResponseEntity.ok(duelDTO);
        } catch (Exception e) {
            DuelDTO error = new DuelDTO();
            error.setErrorMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @PutMapping("/api/duel/join")
    public ResponseEntity<DuelDTO> joinDuel(@RequestParam Long duelId, @RequestParam Long userId) {
        try {
            if (!duelService.joinDuel(duelId, userId)) {
                DuelDTO error = new DuelDTO();
                error.setErrorMessage("Duel kann nicht gejoined werden, da entweder schon gejoined oder es bereits gestartet ist.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }
            DuelDTO duelDTO = DuelDTO.fromEntity(duelService.getById(duelId).get());
            return ResponseEntity.status(HttpStatus.OK).body(duelDTO);
        } catch (ObjectOptimisticLockingFailureException oe) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Duel-Daten nicht aktuell. Bitte neu laden!");
        } catch (Exception e) {
            DuelDTO error = new DuelDTO();
            error.setErrorMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/api/duel/tojoin")
    public ResponseEntity<List<DuelDTO>> getDuelsToJoin(@RequestParam Long userId) {
        try {
            List<Duel> duels = duelService.duelsToJoin(userId);
            List<DuelDTO> duelDTOS = duels.stream()
                    .map(DuelDTO::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(duelDTOS);
        } catch (Exception e) {
            DuelDTO error = new DuelDTO();
            error.setErrorMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(List.of(error));
        }
    }

    @GetMapping("/api/duel/tostart")
    public ResponseEntity<List<DuelDTO>> getDuelsToStart(@RequestParam Long userId) {
        try {
            List<Duel> duels = duelService.duelsToStart(userId);
            List<DuelDTO> duelDTOS = duels.stream()
                    .map(DuelDTO::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(duelDTOS);
        } catch (Exception e) {
            DuelDTO error = new DuelDTO();
            error.setErrorMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(List.of(error));
        }
    }

    @GetMapping("/api/duel/toplay")
    public ResponseEntity<List<DuelDTO>> getDuelToPlay(@RequestParam Long userId){
        try{
            List<Duel> duels = duelService.duelsToPlay(userId);
            List<DuelDTO> duelDtos = duels.stream()
                    .map(DuelDTO::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(duelDtos);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/api/duel/start")
    public ResponseEntity<DuelDTO> startDuel(@RequestParam Long duelId, @RequestParam Long userId) {
        try {
            Duel duel = duelService.startDuelRest(duelId, userId);
            DuelDTO duelDto = DuelDTO.fromEntity(duel);
            return ResponseEntity.status(HttpStatus.OK).body(duelDto);
        } catch (ObjectOptimisticLockingFailureException oe) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Duel-Daten nicht aktuell. Bitte neu laden!");
        } catch (Exception e) {
            DuelDTO error = new DuelDTO();
            error.setErrorMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/api/duel/notPlayedRounds")
    public ResponseEntity<List<RoundDTO>> notPlayedRounds(@RequestParam Long duelId, @RequestParam Long userId) {
        try{
            List<Round> notPlayedRounds = duelService.getNotPlayedRounds(duelId, userId);
            List<RoundDTO> roundDtos = new ArrayList<>();
            for(Round round : notPlayedRounds){
                roundDtos.add(RoundDTO.fromList(duelService.playRoundRest(round.getRoundId())));
            }
            return ResponseEntity.ok(roundDtos);
        } catch (RuntimeException e){
            RoundDTO error = new RoundDTO();
            error.setErrorMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(List.of(error));
        }
    }

    @GetMapping("/api/duel/playround")
    public ResponseEntity<RoundDTO> playRound(@RequestParam Long roundId) {
        try {
            List<String> list = duelService.playRoundRest(roundId);
            RoundDTO roundDTO = RoundDTO.fromList(list);
            return ResponseEntity.ok(roundDTO);
        } catch (Exception e) {
            RoundDTO error = new RoundDTO();
            error.setErrorMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @PostMapping("/api/duel/answer")
    public ResponseEntity<DuelDTO> playerAnswer(@RequestBody AnswerDTO request) {
        try {
            if (duelService.saveSelectedAnswerRest(request.getAnswer(), request.getRoundId(), request.getPlayerId())) {
                return ResponseEntity.ok().build();
            } else {
                DuelDTO error = new DuelDTO();
                error.setErrorMessage("Spieler mit id " + request.getPlayerId() + " hat bereits eine Antwort für Runde " + request.getRoundId() + "abgegeben.");
                return ResponseEntity.internalServerError().body(error);
            }
        } catch (Exception e) {
            DuelDTO error = new DuelDTO();
            error.setErrorMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @DeleteMapping("/api/duel")
    public ResponseEntity<DuelDTO> playerAnswer(@RequestParam Long duelId) {
        try {
            if(duelService.deleteDuel(duelId)){
                return ResponseEntity.ok().build();
            }else{
                DuelDTO error = new DuelDTO();
                error.setErrorMessage("Duel mit id " + duelId + " existiert nicht.");
                return ResponseEntity.internalServerError().body(error);
            }
        } catch (Exception e) {
            DuelDTO error = new DuelDTO();
            error.setErrorMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
