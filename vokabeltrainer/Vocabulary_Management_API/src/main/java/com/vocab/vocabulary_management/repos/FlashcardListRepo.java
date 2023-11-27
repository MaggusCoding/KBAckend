package com.vocab.vocabulary_management.repos;

import com.vocab.vocabulary_management.entities.FlashcardList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FlashcardListRepo extends JpaRepository<FlashcardList, Long> {
    FlashcardList findByCategory(String category);

    @Query("select count(d) from Duel d where d.flashcardsForDuel.flashcardListId = :id")
    long countDuelByFlashcardList(Long id);
}
