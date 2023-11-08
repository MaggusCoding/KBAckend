package com.vocabduel.vocabulary_duel_impl.services;

import com.vocab.vocabulary_duel.entities.Round;
import com.vocab.vocabulary_duel.services.RoundService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoundServiceImpl implements RoundService {
    /**
     * {@inheritDoc}
     */
    @Override
    public Round createRound(Round round) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> generateWrongAnswers(Round round) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Round getById(Long id) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Round> getAllRoundsByDuel(Long duelId) {
        return null;
    }
}
