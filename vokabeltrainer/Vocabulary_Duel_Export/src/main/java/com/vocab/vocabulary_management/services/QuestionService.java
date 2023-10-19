package com.vocab.vocabulary_management.services;

import com.vocab.vocabulary_management.entities.Question;
import java.util.List;

public interface QuestionService {

    Question createQuestion(Question question);

    Question getById(Long id);

    List<Question> getAll();
}
