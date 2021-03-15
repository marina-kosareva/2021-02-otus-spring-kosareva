package ru.otus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.configuration.QuestionProperties;
import ru.otus.model.Question;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class DefaultQuestionnaireService implements QuestionnaireService {

    private final QuestionService questionService;
    private final EvaluationService evaluationService;
    private final MapperService mapperService;
    private final InputOutputService inputOutputService;
    private final QuestionProperties questionProperties;
    private final PrintService printService;

    @Override
    public String getUserName() {
        printService.printLocalizedMessage("interview.nameQuestion");
        return inputOutputService.readFromInput();
    }

    @Override
    public void interview(String userName) {
        AtomicInteger scoreResult = new AtomicInteger(0);

        questionService.getQuestions(questionProperties.getSize())
                .forEach(question -> {
                    int scoreByQuestion = askAndEvaluateAnswer(question);
                    scoreResult.addAndGet(scoreByQuestion);
                });

        showScore(userName, scoreResult.get());
    }

    private int askAndEvaluateAnswer(Question question) {
        showQuestion(question);
        String userAnswer = inputOutputService.readFromInput();
        return evaluationService.evaluate(question, userAnswer);
    }

    private void showQuestion(Question question) {
        printService.printMessage(mapperService.mapQuestionToString(question));
    }

    private void showScore(String userName, int score) {
        String testResultMessageCode = score > questionProperties.getThreshold()
                ? "interview.score.passed"
                : "interview.score.failed";
        printService.printLocalizedMessage(testResultMessageCode, userName, score);
    }
}
