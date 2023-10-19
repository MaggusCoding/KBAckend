package com.vocab.vocabulary_management.services;

import com.vocab.vocabulary_management.entities.Answer;
import java.util.List;

public interface AnswerService {

    Answer createAnswer(Answer answer);

    Answer getById(Long id);

    List<Answer> getAll();

}
