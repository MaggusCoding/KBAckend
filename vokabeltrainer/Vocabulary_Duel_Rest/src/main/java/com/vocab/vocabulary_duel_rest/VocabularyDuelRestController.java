package com.vocab.vocabulary_duel_rest;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management.exceptions.UserNotExistException;
import com.vocab.vocabulary_duel_API.dto.AnswerDTO;
import com.vocab.vocabulary_duel_API.dto.DuelCreateRequest;
import com.vocab.vocabulary_duel_API.dto.DuelDTO;
import com.vocab.vocabulary_duel_API.dto.RoundDTO;
import com.vocab.vocabulary_duel_API.entities.Duel;
import com.vocab.vocabulary_duel_API.entities.Round;
import com.vocab.vocabulary_duel_API.exceptions.*;
import com.vocab.vocabulary_duel_API.services.DuelService;
import com.vocab.vocabulary_management.exceptions.FlashcardListNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<DuelDTO> createDuel(@RequestBody DuelCreateRequest request) throws FlashcardListNotExistException, UserNotExistException, DuelNotExistException {
        Duel duel = duelService.createDuel(request.getUserId(), request.getFlashcardListId());
        duelService.generateRounds(duel.getDuelId());
        Duel duelCreated = duelService.getById(duel.getDuelId()).get();
        return ResponseEntity.status(HttpStatus.CREATED).body(DuelDTO.fromEntity(duelCreated));
    }

    @GetMapping("/api/duel")
    public ResponseEntity<List<DuelDTO>> getAllDuels() {
        List<Duel> duels = duelService.getAll();
        List<DuelDTO> duelDtoList = duels.stream().map(DuelDTO::fromEntity).toList();
        return ResponseEntity.ok(duelDtoList);
    }

    @GetMapping("/api/duel/winners")
    public ResponseEntity<List<UserEntity>> getWinners(@RequestParam Long duelId) throws DuelNotExistException {
        List<UserEntity> userEntities = duelService.calculateWinner(duelId);
        return ResponseEntity.ok(userEntities);
    }

    @GetMapping("/api/duel/byid")
    public ResponseEntity<DuelDTO> getDuelById(@RequestParam Long duelId) {
        Duel duel = duelService.getById(duelId).get();
        DuelDTO duelDTO = DuelDTO.fromEntity(duel);
        return ResponseEntity.ok(duelDTO);
    }

    @PutMapping("/api/duel/join")
    public ResponseEntity<DuelDTO> joinDuel(@RequestParam Long duelId, @RequestParam Long userId) throws UserAlreadyPartOfDuelException, DuelAlreadyStartedException, UserNotExistException, DuelNotExistException {
        duelService.joinDuel(duelId, userId);
        DuelDTO duelDTO = DuelDTO.fromEntity(duelService.getById(duelId).get());
        return ResponseEntity.status(HttpStatus.OK).body(duelDTO);
    }

    @GetMapping("/api/duel/tojoin")
    public ResponseEntity<List<DuelDTO>> getDuelsToJoin(@RequestParam Long userId) throws UserNotExistException {
        List<Duel> duels = duelService.duelsToJoin(userId);
        List<DuelDTO> duelDTOS = duels.stream()
                .map(DuelDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(duelDTOS);
    }

    @GetMapping("/api/duel/tostart")
    public ResponseEntity<List<DuelDTO>> getDuelsToStart(@RequestParam Long userId) throws UserNotExistException {
        List<Duel> duels = duelService.duelsToStart(userId);
        List<DuelDTO> duelDTOS = duels.stream()
                .map(DuelDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(duelDTOS);
    }

    @GetMapping("/api/duel/toplay")
    public ResponseEntity<List<DuelDTO>> getDuelToPlay(@RequestParam Long userId) throws UserNotExistException {
        List<Duel> duels = duelService.duelsToPlay(userId);
        List<DuelDTO> duelDtos = duels.stream()
                .map(DuelDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(duelDtos);
    }

    @PutMapping("/api/duel/start")
    public ResponseEntity<DuelDTO> startDuel(@RequestParam Long duelId, @RequestParam Long userId) throws UserNotPartOfDuelException, DuelAlreadyStartedException, UserNotExistException, DuelNotExistException {
        Duel duel = duelService.startDuelRest(duelId, userId);
        DuelDTO duelDto = DuelDTO.fromEntity(duel);
        return ResponseEntity.status(HttpStatus.OK).body(duelDto);
    }

    @GetMapping("/api/duel/notPlayedRounds")
    public ResponseEntity<List<RoundDTO>> notPlayedRounds(@RequestParam Long duelId, @RequestParam Long userId) throws RoundNotExistException, UserNotExistException, DuelNotExistException {
        List<Round> notPlayedRounds = duelService.getNotPlayedRounds(duelId, userId);
        List<RoundDTO> roundDtos = new ArrayList<>();
        for (Round round : notPlayedRounds) {
            roundDtos.add(RoundDTO.fromList(duelService.playRoundRest(round.getRoundId())));
        }
        return ResponseEntity.ok(roundDtos);
    }

    @GetMapping("/api/duel/playround")
    public ResponseEntity<RoundDTO> playRound(@RequestParam Long roundId) throws RoundNotExistException {
        List<String> list = duelService.playRoundRest(roundId);
        RoundDTO roundDTO = RoundDTO.fromList(list);
        return ResponseEntity.ok(roundDTO);
    }

    @PostMapping("/api/duel/answer")
    public ResponseEntity<DuelDTO> playerAnswer(@RequestBody AnswerDTO request) throws UserAlreadyPlayedRoundException, UserNotExistException, RoundNotExistException {
        duelService.saveSelectedAnswerRest(request.getAnswer(), request.getRoundId(), request.getPlayerId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/duel")
    public ResponseEntity<DuelDTO> deleteDuel(@RequestParam Long duelId) throws DuelNotExistException {
        duelService.deleteDuel(duelId);
        return ResponseEntity.ok().build();
    }
}
