package com.vocab.vocabulary_duel_API.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoundDTO {
    String question;
    String wrongAnswer1;
    String wrongAnswer2;
    String wrongAnswer3;
    String correctAnswer;

    public static RoundDTO fromList(List<String> list){
        RoundDTO roundDTO = new RoundDTO();
        roundDTO.setQuestion(list.get(0));
        roundDTO.setWrongAnswer1(list.get(1));
        roundDTO.setWrongAnswer2(list.get(2));
        roundDTO.setWrongAnswer3(list.get(3));
        roundDTO.setCorrectAnswer(list.get(4));
        return roundDTO;
    }
}
