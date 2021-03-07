package ru.otus.service;

import org.springframework.stereotype.Service;
import ru.otus.model.Answer;
import ru.otus.model.Question;

import java.util.stream.Collectors;

@Service
public class DefaultMapperService implements MapperService {

    @Override
    public String mapQuestionToString(Question question) {
        return String.format("%s Answers: %s",
                question.getTitle(),
                question.getAnswers().stream()
                        .map(this::mapAnswerToString)
                        .collect(Collectors.joining(" ")));
    }

    private String mapAnswerToString(Answer answer) {
        return String.format("%s) %s", answer.getId(), answer.getTitle());
    }
}
