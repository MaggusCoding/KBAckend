package com.vocab.vocabulary_management.repos;

import com.vocab.vocabulary_management.entities.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslationRepo extends JpaRepository<Translation, Long> {
}
