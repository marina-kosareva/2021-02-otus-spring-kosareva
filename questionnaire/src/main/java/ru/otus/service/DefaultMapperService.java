package ru.otus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.model.Answer;
import ru.otus.model.Question;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultMapperService implements MapperService {

    private final LocalizationService localizationService;

    @Override
    public String mapQuestionToString(Question question) {
        return String.format("%s %s: %s", question.getTitle(),
                localizationService.getMessage("answers"),
                question.getAnswers().stream()
                        .map(this::mapAnswerToString)
                        .collect(Collectors.joining(" ")));
    }

    private String mapAnswerToString(Answer answer) {
        return String.format("%s) %s", answer.getId(), answer.getTitle());
    }
}
