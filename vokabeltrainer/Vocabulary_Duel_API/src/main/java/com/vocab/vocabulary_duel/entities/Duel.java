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
import java.util.stream.Collectors;

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

    @OneToMany (mappedBy = "duel", fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE})
    private List<Round> rounds;


    private boolean started;

    private boolean finished;
    public void setPlayer(UserEntity player){
        this.players.add(player);
    }
    @Override
    public String toString() {
        String playerInfo = players != null
                ? players.stream().map(user -> "Player ID: " + user.getUserId()).collect(Collectors.joining(", "))
                : "null";

        return "Duel{" +
                "duelId=" + duelId +
                ", flashcardsForDuel=" + (flashcardsForDuel != null ? flashcardsForDuel.getFlashcardListId() : null) +
                ", players=" + playerInfo +
                ", started=" + started +
                ", finished=" + finished +
                '}';
    }
}