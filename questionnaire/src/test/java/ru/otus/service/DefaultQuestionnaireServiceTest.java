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
    @Mock
    private LocalizationService localizationService;

    @Test
    void interview() {
        QuestionnaireService service = new DefaultQuestionnaireService(questionService,
                evaluationService, mapperService, inputOutputService, localizationService, 2, 1);

        Question question1 = mock(Question.class);
        Question question2 = mock(Question.class);

        when(localizationService.getMessage("interview.nameQuestion")).thenReturn("Hello, what is your name?");
        when(localizationService.getMessage("interview.failed")).thenReturn("failed");
        when(localizationService.getMessage("interview.score", "failed", "Marina Kosareva", 1))
                .thenReturn("Test failed. Marina Kosareva, your score is 1");

        when(questionService.getQuestions(2)).thenReturn(asList(question1, question2));
        when(inputOutputService.readFromInput()).thenReturn("Marina Kosareva", "2", "1");
        when(mapperService.mapQuestionToString(question1)).thenReturn("shown question1 with answers");
        when(evaluationService.evaluate(question1, "2")).thenReturn(0);
        when(mapperService.mapQuestionToString(question2)).thenReturn("shown question2 with answers");
        when(evaluationService.evaluate(question2, "1")).thenReturn(1);

        service.interview();

        verify(inputOutputService).writeToOutput("Hello, what is your name?");
        verify(inputOutputService, times(3)).readFromInput();

        verify(questionService).getQuestions(2);

        verify(mapperService).mapQuestionToString(question1);
        verify(inputOutputService).writeToOutput("shown question1 with answers");
        verify(evaluationService).evaluate(question1, "2");

        verify(mapperService).mapQuestionToString(question2);
        verify(inputOutputService).writeToOutput("shown question2 with answers");
        verify(evaluationService).evaluate(question2, "1");

        verify(localizationService).getMessage("interview.nameQuestion");
        verify(localizationService).getMessage("interview.failed");
        verify(localizationService).getMessage("interview.score", "failed", "Marina Kosareva", 1);

        verify(inputOutputService).writeToOutput("Test failed. Marina Kosareva, your score is 1");

        verifyNoMoreInteractions(inputOutputService, questionService, evaluationService);
    }
}
