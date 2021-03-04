package ru.otus.service;

import ru.otus.model.Question;

public interface EvaluationService {

    int evaluate(Question question, String userAnswer);
}
