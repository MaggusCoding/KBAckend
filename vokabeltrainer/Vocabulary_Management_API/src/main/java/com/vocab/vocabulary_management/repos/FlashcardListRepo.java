package com.vocab.vocabulary_management.repos;

import com.vocab.vocabulary_management.entities.FlashcardList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlashcardListRepo extends JpaRepository<FlashcardList, Long> {
}
