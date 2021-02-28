package ru.otus.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.dao.QuestionDao;
import ru.otus.model.Question;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultQuestionServiceTest {

    @Mock
    private QuestionDao questionDao;

    @InjectMocks
    private DefaultQuestionService service;

    @Test
    void interview() {
        Question question1 = mock(Question.class);
        Question question2 = mock(Question.class);

        when(questionDao.getQuestions()).thenReturn(asList(question1, question2));

        List<Question> result = service.getQuestions();

        assertThat(result).containsExactlyInAnyOrder(question1, question2);

        verify(questionDao).getQuestions();
    }

}
