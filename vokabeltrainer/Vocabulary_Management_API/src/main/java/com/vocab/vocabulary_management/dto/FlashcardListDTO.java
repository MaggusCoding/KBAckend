package com.vocab.vocabulary_management.dto;

import com.vocab.vocabulary_management.entities.FlashcardList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashcardListDTO {
    private Long flashcardListId;
    private String flashcardListName;
    private String content;

    public static FlashcardListDTO fromEntity(FlashcardList flashcardList){
        FlashcardListDTO flashcardListDTO = new FlashcardListDTO();
        flashcardListDTO.setFlashcardListId(flashcardList.getFlashcardListId());
        flashcardListDTO.setFlashcardListName(flashcardList.getCategory());
        return flashcardListDTO;
    }
}
