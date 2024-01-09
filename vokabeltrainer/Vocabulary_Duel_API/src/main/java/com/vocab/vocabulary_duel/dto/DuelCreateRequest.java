package com.vocab.vocabulary_duel.dto;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.vocabulary_duel.entities.Round;
import com.vocab.vocabulary_management.entities.FlashcardList;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DuelCreateRequest {


    private Long createPlayer;
    private Long flashcardsForDuel;

    private boolean started;

    private boolean finished;

}