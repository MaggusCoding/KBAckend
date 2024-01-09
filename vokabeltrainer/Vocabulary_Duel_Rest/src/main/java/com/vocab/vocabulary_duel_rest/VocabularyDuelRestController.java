package com.vocab.vocabulary_duel_rest;

import com.vocab.vocabulary_duel_API.dto.DuelCreateRequest;
import com.vocab.vocabulary_duel_API.entities.Duel;
import com.vocab.vocabulary_duel_impl.services.DuelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@ComponentScan("com.vocab")
public class VocabularyDuelRestController {
    @Autowired
    DuelServiceImpl duelService;

    @PostMapping("/api/duel")
    public ResponseEntity<Void> createDuel(@RequestBody DuelCreateRequest request) throws URISyntaxException {
        try {
            Duel duel = duelService.createDuel(request.getCreatePlayer(), request.getFlashcardsForDuel());
            URI uri = new URI("/api/duel/" + duel.getDuelId());
            return ResponseEntity.created(uri).build();
        }catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
