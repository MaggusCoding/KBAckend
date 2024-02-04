package com.vocab.vocabulary_duel_API.dto;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.vocabulary_duel_API.entities.Duel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DuelDTO {

    private Long duelId;
    private List<Long> winnerIds;
    private List<String> winnerUsernames;
    private List<Long> playerIds;
    private List<String> playerUsernames;
    private String flashcardListName;
//    private Long flashcardsForDuelId;
    private boolean started;
    private boolean finished;

    public static DuelDTO fromEntity(Duel duel) {
        DuelDTO dto = new DuelDTO();
        dto.setDuelId(duel.getDuelId());
        dto.setWinnerIds(duel.getWinner().stream().map(UserEntity::getUserId).collect(Collectors.toList()));
        dto.setWinnerUsernames(duel.getWinner().stream().map(UserEntity::getUsername).collect(Collectors.toList()));
        dto.setPlayerIds(duel.getPlayers().stream().map(UserEntity::getUserId).collect(Collectors.toList()));
        dto.setPlayerUsernames(duel.getPlayers().stream().map(UserEntity::getUsername).collect(Collectors.toList()));
//        dto.setFlashcardsForDuelId(duel.getFlashcardsForDuel().getFlashcardListId());
        dto.setFlashcardListName(duel.getFlashcardsForDuel().getCategory());
        dto.setStarted(duel.isStarted());
        dto.setFinished(duel.isFinished());

        return dto;
    }

}
