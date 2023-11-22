package com.vocab.vocabulary_duel.entities;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.vocabulary_management.entities.FlashcardList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Duel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long duelId;

    @ManyToMany
    private List<UserEntity> winner;

    @ManyToMany
    private List<UserEntity> players;

    @ManyToOne
    @JoinColumn(name="flashcardListId", nullable=false)
    private FlashcardList flashcardsForDuel;

    private boolean isStarted;

    public void setPlayer(UserEntity player){
        this.players.add(player);
    }

}