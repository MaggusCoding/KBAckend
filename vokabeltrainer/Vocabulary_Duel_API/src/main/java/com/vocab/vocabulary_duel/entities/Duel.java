package com.vocab.vocabulary_duel.entities;

import com.management.user_management.entities.User;
import com.vocab.vocabulary_management.entities.Flashcard;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
@Entity
@Table(name = "duel")
@Data
@NoArgsConstructor

public class Duel {

    @Id
    private Long duelId;

    @OneToMany(mappedBy = "user")
    private List<User> winner;

    @OneToMany(mappedBy = "user")
    private List<User> players;

    @OneToMany(mappedBy = "flashcard")
    private List<Flashcard> flashcardsForDuel;

    public Duel(Long duelId, List<User> players, List<Flashcard> flashcardsForDuel) {
        this.duelId = duelId;
        this.players = players;
        this.flashcardsForDuel = flashcardsForDuel;
    }


}