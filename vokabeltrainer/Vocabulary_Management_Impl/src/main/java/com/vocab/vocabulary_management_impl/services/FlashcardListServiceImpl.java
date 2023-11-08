package com.vocab.vocabulary_management_impl.services;

import com.vocab.vocabulary_management.entities.Flashcard;
import com.vocab.vocabulary_management.entities.FlashcardList;
import com.vocab.vocabulary_management.services.FlashcardListService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FlashcardListServiceImpl implements FlashcardListService {
    private String default_path = "/vocabFiles/";
    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean createFlashcardList(String filename) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FlashcardList getById(Long id) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FlashcardList> getAll() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Flashcard> getFlashcardsByFlashcardListId(Long id) {
        return null;
    }
}
