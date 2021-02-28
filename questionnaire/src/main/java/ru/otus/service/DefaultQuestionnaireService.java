package ru.otus.service;

import java.io.OutputStreamWriter;
import java.io.Writer;

import static ru.otus.utils.StreamsUtils.close;
import static ru.otus.utils.StreamsUtils.writeToOutput;

public class DefaultQuestionnaireService implements QuestionnaireService {

    private final QuestionService questionService;
    private Writer writer;

    public DefaultQuestionnaireService(QuestionService questionService) {
        this.questionService = questionService;
        this.writer = new OutputStreamWriter(System.out);
    }

    @Override
    public void interview() {
        showQuestions();

        close(writer);
    }

    private void showQuestions() {
        questionService.getQuestions()
                .forEach(question -> writeToOutput(question.toString(), writer));
    }
}
