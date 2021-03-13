package ru.otus.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.configuration.QuestionProperties;
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
    private QuestionProperties questionProperties;
    @Mock
    private PrintService printService;

    @InjectMocks
    DefaultQuestionnaireService service;

    @Test
    void interview() {

        Question question1 = mock(Question.class);
        Question question2 = mock(Question.class);

        when(questionProperties.getSize()).thenReturn(2);
        when(questionProperties.getThreshold()).thenReturn(1);

        when(questionService.getQuestions(2)).thenReturn(asList(question1, question2));
        when(inputOutputService.readFromInput()).thenReturn("Marina Kosareva", "2", "1");
        when(mapperService.mapQuestionToString(question1)).thenReturn("shown question1 with answers");
        when(evaluationService.evaluate(question1, "2")).thenReturn(0);
        when(mapperService.mapQuestionToString(question2)).thenReturn("shown question2 with answers");
        when(evaluationService.evaluate(question2, "1")).thenReturn(1);

        service.interview();

        verify(inputOutputService, times(3)).readFromInput();

        verify(questionService).getQuestions(2);

        verify(mapperService).mapQuestionToString(question1);
        verify(printService).printMessage("shown question1 with answers");
        verify(evaluationService).evaluate(question1, "2");

        verify(mapperService).mapQuestionToString(question2);
        verify(printService).printMessage("shown question2 with answers");
        verify(evaluationService).evaluate(question2, "1");

        verify(printService).printLocalizedMessage("interview.nameQuestion");
        verify(printService).printLocalizedMessage("interview.score.failed", "Marina Kosareva", 1);

        verifyNoMoreInteractions(inputOutputService, questionService, evaluationService);
    }
}
