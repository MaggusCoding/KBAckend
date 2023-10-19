package com.vocab.vocabulary_management.services;

import com.vocab.vocabulary_management.entities.Round;
import java.util.List;

public interface RoundService {

    Round createRound(Round round);

    Round getById(Long id);

    List<Round> getAll();

}
