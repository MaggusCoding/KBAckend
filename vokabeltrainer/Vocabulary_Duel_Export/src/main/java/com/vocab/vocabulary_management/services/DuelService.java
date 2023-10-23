package com.vocab.vocabulary_management.services;

import com.management.user_management.entities.User;
import com.vocab.vocabulary_management.entities.Duel;
import java.util.List;

public interface DuelService {

    Duel createDuel(Duel duel);

    Boolean joinDuel(Long duelId, Long userId);

    Duel getById(Long id);

    List<Duel> getAll();

    User calculateWinner(Duel duel);

}
