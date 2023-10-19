package com.vocab.vocabulary_management.services;

import com.vocab.vocabulary_management.entities.Duel;
import java.util.List;

public interface DuelService {

    Duel createDuel(Duel duel);

    Duel getById(Long id);

    List<Duel> getAll();

}
