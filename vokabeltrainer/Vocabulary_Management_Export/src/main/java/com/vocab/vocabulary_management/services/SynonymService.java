package com.vocab.vocabulary_management.services;

import com.vocab.vocabulary_management.entities.Synonym;
import java.util.List;

public interface SynonymService {

    Synonym createSynonym(Synonym synonyme);

    Synonym getById(Long id);

    List<Synonym> getAll();

    List<String> getSynonymsByOriginalText(String originalText);

}