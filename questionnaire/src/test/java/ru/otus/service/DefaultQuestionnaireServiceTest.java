package ru.otus.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.dao.Parser;
import ru.otus.model.Answer;
import ru.otus.model.Question;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultQuestionnaireServiceTest {

    private static final String FILE_NAME = "fileName";

    @Mock
    private Parser parser;

    @InjectMocks
    private DefaultQuestionnaireService service;

    @Test
    void questionsToDisplay() {
        service.setFileName(FILE_NAME);

        Question question1 = question("Are you happy?",
                answer(1, "yes"), answer(2, "no"));
        Question question2 = question("What is your name?");

        when(parser.parse(FILE_NAME)).thenReturn(asList(question1, question2));

        List<String> result = service.questionsToDisplay();

        assertThat(result).containsExactlyInAnyOrder("Question: Are you happy? Answer: 1)yes 2)no",
                "Question: What is your name? Answer: ");

        verify(parser).parse(FILE_NAME);
    }

    private Answer answer(int id, String title) {
        Answer answer = new Answer();
        answer.setId(id);
        answer.setTitle(title);
        return answer;
    }

    private Question question(String title, Answer... answers) {
        Question question = new Question();
        question.setTitle(title);
        question.setAnswers(asList(answers));
        return question;
    }
}
