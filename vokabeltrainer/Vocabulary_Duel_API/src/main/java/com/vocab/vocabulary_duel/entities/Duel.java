package com.vocab.vocabulary_duel.entities;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.vocabulary_management.entities.FlashcardList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Duel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long duelId;

    @ManyToMany (fetch = FetchType.EAGER)
    private List<UserEntity> winner= new ArrayList<>();

    @ManyToMany (fetch = FetchType.EAGER)
    private List<UserEntity> players = new ArrayList<>();

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name="flashcardListId", nullable=false)
    private FlashcardList flashcardsForDuel;

    private boolean isStarted;

    public void setPlayer(UserEntity player){
        this.players.add(player);
    }

}