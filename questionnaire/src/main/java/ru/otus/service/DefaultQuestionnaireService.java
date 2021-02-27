package ru.otus.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.otus.dao.Parser;
import ru.otus.model.Answer;
import ru.otus.model.Question;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@RequiredArgsConstructor
public class DefaultQuestionnaireService implements QuestionnaireService {

    private final Parser parser;
    private String fileName;

    @Override
    public List<String> questionsToDisplay() {
        return parser.parse(fileName).stream()
                .map(this::questionToDisplay)
                .collect(Collectors.toList());
    }

    private String questionToDisplay(Question question) {
        return "Question: " + question.getTitle() + " Answer: " + answers(question.getAnswers());
    }

    private String answers(List<Answer> answers) {
        return answers.stream()
                .filter(answer -> answer.getTitle() != null)
                .map(answer -> answer.getId() + ")" + answer.getTitle())
                .collect(Collectors.joining(" "));
    }
}
