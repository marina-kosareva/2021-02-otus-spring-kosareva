package ru.otus.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.configuration.QuestionProperties;
import ru.otus.exceptions.QuestionsLoadingException;
import ru.otus.model.Answer;
import ru.otus.model.Question;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvQuestionDaoTest {

    @Mock
    private QuestionProperties properties;

    @InjectMocks
    private CsvQuestionDao dao;

    @Test
    void getQuestions() {
        when(properties.getFileName()).thenReturn("testQuestions.csv");

        List<Question> questions = dao.getQuestions();

        Question expected1 = Question.builder()
                .id(1)
                .correctAnswer(1)
                .title("Question1")
                .answers(answers())
                .build();

        Question expected2 = Question.builder()
                .id(2)
                .correctAnswer(1)
                .title("Question2")
                .answers(answers())
                .build();

        assertThat(questions).containsExactlyInAnyOrder(expected1, expected2);
    }

    @Test
    void getQuestions_fileNotFound() {
        when(properties.getFileName()).thenReturn("unknown");

        Exception exception = assertThrows(QuestionsLoadingException.class, dao::getQuestions);

        assertEquals("File with name unknown is not found", exception.getMessage());
    }

    @Test
    void getQuestions_emptyFile() {
        when(properties.getFileName()).thenReturn("emptyQuestions.csv");

        List<Question> questions = dao.getQuestions();

        assertThat(questions).isEmpty();
    }

    @Test
    void getQuestions_incorrectFormatFile() {
        when(properties.getFileName()).thenReturn("incorrectFormatQuestions.csv");

        Exception exception = assertThrows(QuestionsLoadingException.class, dao::getQuestions);

        assertEquals("Csv parsing error", exception.getMessage());
    }

    private List<Answer> answers() {
        return asList(answer(1, "answer1"),
                answer(2, "answer2"),
                answer(3, "answer3"));
    }

    private Answer answer(int id, String title) {
        return Answer.builder()
                .id(id)
                .title(title)
                .build();
    }
}
