package com.vocabduel.vocabulary_duel_impl.services;

import com.management.user_management.entities.User;
import com.vocab.vocabulary_duel.entities.Duel;
import com.vocab.vocabulary_duel.services.DuelService;
import com.vocab.vocabulary_management.entities.Flashcard;

import java.util.List;

public class DuelServiceImpl implements DuelService {
    /**
     * {@inheritDoc}
     */
    @Override
    public Duel createDuel(Duel duel) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean joinDuel(Long duelId, Long userId) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Duel getById(Long id) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Duel> getAll() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> calculateWinner(Duel duel) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Flashcard> generateFlashcardList() {
        return null;
    }
}
