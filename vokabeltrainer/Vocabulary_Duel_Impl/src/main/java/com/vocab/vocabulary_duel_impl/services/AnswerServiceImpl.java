package com.vocab.vocabulary_duel_impl.services;

import com.vocab.vocabulary_duel.entities.Answer;
import com.vocab.vocabulary_duel.services.AnswerService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AnswerServiceImpl implements AnswerService {
    /**
     * {@inheritDoc}
     */
    @Override
    public Answer createAnswer(Answer answer) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Answer getById(Long id) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Answer> getAllAnswersByRound(Long roundId) {
        return null;
    }
}
