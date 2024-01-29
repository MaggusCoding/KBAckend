package com.vocab.vocabulary_management_impl.repos;

import com.vocab.vocabulary_management.entities.FlashcardList;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FlashcardListRepo extends JpaRepository<FlashcardList, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    FlashcardList findByCategory(String category);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select count(d) from Duel d where d.flashcardsForDuel.flashcardListId = :id")
    long countDuelByFlashcardList(Long id);
}
