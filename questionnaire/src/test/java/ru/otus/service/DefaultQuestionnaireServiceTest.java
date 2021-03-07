package ru.otus.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.model.Question;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultQuestionnaireServiceTest {

    @Mock
    private QuestionService questionService;
    @Mock
    private EvaluationService evaluationService;
    @Mock
    private InputOutputService inputOutputService;
    @Mock
    private MapperService mapperService;

    @Test
    void interview() {
        QuestionnaireService service = new DefaultQuestionnaireService(questionService,
                evaluationService, mapperService, inputOutputService, 2, 1);

        Question question1 = mock(Question.class);
        Question question2 = mock(Question.class);

        when(questionService.getQuestions(2)).thenReturn(asList(question1, question2));
        when(inputOutputService.readFromInput(System.in)).thenReturn("Marina Kosareva", "2", "1");
        when(mapperService.mapQuestionToString(question1)).thenReturn("shown question1 with answers");
        when(evaluationService.evaluate(question1, "2")).thenReturn(0);
        when(mapperService.mapQuestionToString(question2)).thenReturn("shown question2 with answers");
        when(evaluationService.evaluate(question2, "1")).thenReturn(1);

        service.interview();

        verify(inputOutputService).writeToOutput(System.out, "Hello, what is your name?");
        verify(inputOutputService, times(3)).readFromInput(System.in);

        verify(questionService).getQuestions(2);

        verify(mapperService).mapQuestionToString(question1);
        verify(inputOutputService).writeToOutput(System.out, "shown question1 with answers");
        verify(evaluationService).evaluate(question1, "2");

        verify(mapperService).mapQuestionToString(question2);
        verify(inputOutputService).writeToOutput(System.out, "shown question2 with answers");
        verify(evaluationService).evaluate(question2, "1");

        verify(inputOutputService).writeToOutput(System.out, "Test failed. Marina Kosareva, your score is 1");

        verifyNoMoreInteractions(inputOutputService, questionService, evaluationService);
    }
}
