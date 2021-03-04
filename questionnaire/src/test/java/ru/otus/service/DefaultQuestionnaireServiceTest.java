package ru.otus.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.model.Answer;
import ru.otus.model.Question;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultQuestionnaireServiceTest {

    @Mock
    private QuestionService questionService;
    @Mock
    private InputOutputService inputOutputService;
    @Mock
    private EvaluationService evaluationService;

    @Test
    void interview() {
        QuestionnaireService service = new DefaultQuestionnaireService(questionService,
                inputOutputService, evaluationService, 2, 1);

        Question question1 = Question.builder()
                .id(1)
                .correctAnswer(1)
                .title("Question1")
                .answers(asList(answer(1, "answer1"),
                        answer(2, "answer2"),
                        answer(3, "answer3")))
                .build();
        Question question2 = Question.builder()
                .id(2)
                .correctAnswer(1)
                .title("Question2")
                .answers(asList(answer(1, "answer1"),
                        answer(2, "answer2"),
                        answer(3, "answer3")))
                .build();
        Question question3 = mock(Question.class);

        when(questionService.getQuestions()).thenReturn(asList(question1, question2, question3));
        when(inputOutputService.readFromInput()).thenReturn("Marina Kosareva", "2", "1");
        when(evaluationService.evaluate(question1, "2")).thenReturn(0);
        when(evaluationService.evaluate(question2, "1")).thenReturn(1);

        service.interview();

        verify(inputOutputService).writeToOutput("Hello, what is your name?");
        verify(inputOutputService, times(3)).readFromInput();
        verify(questionService).getQuestions();
        verify(inputOutputService).writeToOutput(question1.toString());
        verify(evaluationService).evaluate(question1, "2");
        verify(inputOutputService).writeToOutput(question2.toString());
        verify(evaluationService).evaluate(question2, "1");
        verify(inputOutputService).writeToOutput("Test failed. Your score is 1");

        verifyNoMoreInteractions(inputOutputService, questionService, evaluationService);
    }

    private Answer answer(int id, String title) {
        return Answer.builder()
                .id(id)
                .title(title)
                .build();
    }
}
