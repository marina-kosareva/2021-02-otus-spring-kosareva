package ru.otus.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.model.Question;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DefaultQuestionnaireService implements QuestionnaireService {

    private final QuestionService questionService;
    private final EvaluationService evaluationService;
    private final MapperService mapperService;
    private final InputOutputService inputOutputService;
    private final int questionsNumber;
    private final int threshold;

    DefaultQuestionnaireService(QuestionService questionService,
                                EvaluationService evaluationService,
                                MapperService mapperService,
                                InputOutputService inputOutputService,
                                @Value("${questions.size}") int questionsNumber,
                                @Value("${threshold}") int threshold) {
        this.questionService = questionService;
        this.evaluationService = evaluationService;
        this.mapperService = mapperService;
        this.inputOutputService = inputOutputService;
        this.questionsNumber = questionsNumber;
        this.threshold = threshold;
    }

    @Override
    public void interview() {
        AtomicInteger scoreResult = new AtomicInteger(0);

        String userName = getUserName();

        questionService.getQuestions(questionsNumber)
                .forEach(question -> {
                    int scoreByQuestion = askAndEvaluateAnswer(question);
                    scoreResult.addAndGet(scoreByQuestion);
                });

        showScore(userName, scoreResult.get());

    }

    private String getUserName() {
        inputOutputService.writeToOutput("Hello, what is your name?");
        return inputOutputService.readFromInput();
    }

    private int askAndEvaluateAnswer(Question question) {
        showQuestion(question);
        String userAnswer = inputOutputService.readFromInput();
        return evaluationService.evaluate(question, userAnswer);
    }

    private void showQuestion(Question question) {
        inputOutputService.writeToOutput(mapperService.mapQuestionToString(question));
    }

    private void showScore(String userName, int score) {
        String testResultMessage = score > threshold ? "passed" : "failed";
        inputOutputService.writeToOutput(String.format("Test %s. %s, your score is %s", testResultMessage, userName, score));
    }
}
