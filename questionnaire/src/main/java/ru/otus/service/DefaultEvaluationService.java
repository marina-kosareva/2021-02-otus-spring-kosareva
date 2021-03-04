package ru.otus.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.otus.model.Question;

@Service
public class DefaultEvaluationService implements EvaluationService {

    @Override
    public int evaluate(Question question, String userAnswer) {
        if (StringUtils.equals(userAnswer, String.valueOf(question.getCorrectAnswer()))) {
            return 1;
        }
        return 0;
    }
}
