package com.management.user_management.entities;

import com.vocab.vocabulary_duel.entities.Duel;
import com.vocab.vocabulary_duel.entities.Round;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class User {
    @Id
    private Long userId;
    private String username;
    @ManyToMany(mappedBy = "winner")
    private List<Duel> duelsWon;
    @ManyToMany(mappedBy = "players")
    private List<Duel> duelsPlayed;
}
