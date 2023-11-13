package com.vocab.vocabulary_management_impl.services;

import com.vocab.vocabulary_management.entities.Translation;
import com.vocab.vocabulary_management.services.TranslationService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TranslationServiceImpl implements TranslationService {

    /**
     * {@inheritDoc}
     */
    @Override
    public Translation createTranslation(Translation translation) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Translation getById(Long id) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Translation> getAllTranslationsByFlashcard(Long flashcardId) {
        return null;
    }

}
