package com.vocab.vocabulary_management.dto;

import com.vocab.vocabulary_management.entities.FlashcardList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashcardListDTO {
    public Long flashcardListId;
    public String flashcardListname;

    public static FlashcardListDTO fromEntity(FlashcardList flashcardList){
        FlashcardListDTO flashcardListDTO = new FlashcardListDTO();
        flashcardListDTO.setFlashcardListId(flashcardList.getFlashcardListId());
        flashcardListDTO.setFlashcardListname(flashcardList.getCategory());
        return flashcardListDTO;
    }
}
