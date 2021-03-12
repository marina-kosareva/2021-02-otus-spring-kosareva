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
    private final LocalizationService localizationService;
    private final int questionsNumber;
    private final int threshold;

    DefaultQuestionnaireService(QuestionService questionService,
                                EvaluationService evaluationService,
                                MapperService mapperService,
                                InputOutputService inputOutputService,
                                LocalizationService localizationService,
                                @Value("${questions.size}") int questionsNumber,
                                @Value("${threshold}") int threshold) {
        this.questionService = questionService;
        this.evaluationService = evaluationService;
        this.mapperService = mapperService;
        this.inputOutputService = inputOutputService;
        this.localizationService = localizationService;
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
        inputOutputService.writeToOutput(localizationService.getMessage("interview.nameQuestion"));
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
        String testResultMessage = score > threshold
                ? localizationService.getMessage("interview.passed")
                : localizationService.getMessage("interview.failed");
        inputOutputService.writeToOutput(localizationService.getMessage("interview.score", testResultMessage, userName, score));
    }
}
