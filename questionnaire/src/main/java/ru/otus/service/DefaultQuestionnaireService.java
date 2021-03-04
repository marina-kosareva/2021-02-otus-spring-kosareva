package ru.otus.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.model.Question;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DefaultQuestionnaireService implements QuestionnaireService {

    private final QuestionService questionService;
    private final InputOutputService streamService;
    private final EvaluationService evaluationService;
    private final int questionsNumber;
    private final int threshold;

    DefaultQuestionnaireService(QuestionService questionService,
                                InputOutputService streamService,
                                EvaluationService evaluationService,
                                @Value("${questions.size}") int questionsNumber,
                                @Value("${threshold}") int threshold) {
        this.questionService = questionService;
        this.streamService = streamService;
        this.evaluationService = evaluationService;
        this.questionsNumber = questionsNumber;
        this.threshold = threshold;
    }

    @Override
    public void interview() {
        AtomicInteger scoreResult = new AtomicInteger(0);

        greet();

        questionService.getQuestions()
                .subList(0, questionsNumber)
                .forEach(question -> {
                    showQuestion(question);
                    int score = evaluationService.evaluate(question, getAnswer());
                    scoreResult.addAndGet(score);
                });

        showScore(scoreResult.get());

    }

    private void greet() {
        streamService.writeToOutput("Hello, what is your name?");
        streamService.readFromInput();
    }

    private void showQuestion(Question question) {
        streamService.writeToOutput(question.toString());
    }

    private String getAnswer() {
        return streamService.readFromInput();
    }

    private void showScore(int score) {
        String testResultMessage = score > threshold ? "passed" : "failed";
        streamService.writeToOutput(String.format("Test %s. Your score is %s", testResultMessage, score));
    }
}
