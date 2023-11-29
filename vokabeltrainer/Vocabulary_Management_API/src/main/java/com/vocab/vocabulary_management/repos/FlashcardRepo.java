package com.vocab.vocabulary_management.repos;

import com.vocab.vocabulary_management.entities.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlashcardRepo extends JpaRepository<Flashcard, Long> {
    Flashcard findByOriginalText(String s);

    @Query("select f from Flashcard f where f.flashcardList.flashcardListId = :id")
    List<Flashcard> findByFlashcardListId(Long id);
}
