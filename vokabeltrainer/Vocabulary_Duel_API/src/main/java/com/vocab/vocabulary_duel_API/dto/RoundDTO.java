package com.vocab.vocabulary_duel_API.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoundDTO {
    private Long roundId;
    private String question;
    private String wrongAnswer1;
    private String wrongAnswer2;
    private String wrongAnswer3;
    private String correctAnswer;
    private String errorMessage;

    public static RoundDTO fromList(List<String> list){
        RoundDTO roundDTO = new RoundDTO();
        roundDTO.setRoundId(Long.valueOf(list.get(0)));
        roundDTO.setQuestion(list.get(1));
        roundDTO.setWrongAnswer1(list.get(2));
        roundDTO.setWrongAnswer2(list.get(3));
        roundDTO.setWrongAnswer3(list.get(4));
        roundDTO.setCorrectAnswer(list.get(5));
        return roundDTO;
    }

}
