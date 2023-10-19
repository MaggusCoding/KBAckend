package com.vocab.vocabulary_management.services;

import com.vocab.vocabulary_management.entities.Translation;
import java.util.List;

public interface TranslationService {

    Translation createTranslation(Translation translation);

    Translation getById(Long id);

    List<Translation> getAll();

}