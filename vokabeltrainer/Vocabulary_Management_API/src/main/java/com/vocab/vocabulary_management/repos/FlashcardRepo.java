package com.vocab.vocabulary_management.repos;

import com.vocab.vocabulary_management.entities.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlashcardRepo extends JpaRepository<Flashcard, Long> {
    Flashcard findByOriginalText(String s);
}
