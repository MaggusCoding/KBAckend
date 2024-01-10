package com.vocab.vocabulary_duel_API.dto;

import com.vocab.vocabulary_duel_API.entities.Duel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import com.vocab.vocabulary_duel_API.entities.Round;
import com.vocab.user_management.entities.UserEntity;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DuelDTO {

    private Long duelId;
    private List<Long> winnerIds;
    private List<String> winnerUsernames;
    private List<Long> playerIds;
    private List<String> playerUsernames;
    private Long flashcardsForDuelId;
    private List<Long> roundIds;
    private boolean started;
    private boolean finished;


    public static DuelDTO fromEntity(Duel duel) {
        DuelDTO dto = new DuelDTO();
        dto.setDuelId(duel.getDuelId());
        dto.setWinnerIds(duel.getWinner().stream().map(UserEntity::getUserId).collect(Collectors.toList()));
        dto.setWinnerUsernames(duel.getWinner().stream().map(UserEntity::getUsername).collect(Collectors.toList()));
        dto.setPlayerIds(duel.getPlayers().stream().map(UserEntity::getUserId).collect(Collectors.toList()));
        dto.setPlayerUsernames(duel.getPlayers().stream().map(UserEntity::getUsername).collect(Collectors.toList()));
        dto.setFlashcardsForDuelId(duel.getFlashcardsForDuel().getFlashcardListId());
        dto.setRoundIds(duel.getRounds().stream().map(Round::getRoundId).collect(Collectors.toList()));
        dto.setStarted(duel.isStarted());
        dto.setFinished(duel.isFinished());

        return dto;
    }
}
