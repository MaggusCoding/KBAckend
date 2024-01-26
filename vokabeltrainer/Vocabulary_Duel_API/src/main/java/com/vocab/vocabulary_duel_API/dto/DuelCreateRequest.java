package com.vocab.vocabulary_duel_API.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DuelCreateRequest {


    private Long userID;
    private Long flashcardListID;


}