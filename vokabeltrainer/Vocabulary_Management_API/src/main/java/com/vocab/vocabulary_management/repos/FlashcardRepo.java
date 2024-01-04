package com.vocab.vocabulary_management.repos;

import com.vocab.vocabulary_management.entities.Flashcard;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlashcardRepo extends JpaRepository<Flashcard, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    Flashcard findByOriginalText(String s);

    @Lock(LockModeType.OPTIMISTIC)
    boolean existsByOriginalText(String text);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select f from Flashcard f where f.flashcardList.flashcardListId = :id")
    List<Flashcard> findByFlashcardListId(Long id);
}
