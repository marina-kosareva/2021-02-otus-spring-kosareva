package ru.otus.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class DefaultQuestionnaireServiceIT {
    private static final String USER_NAME = "Marina Kosareva";

    @MockBean
    private InputOutputService inputOutputService;
    @Autowired
    private QuestionnaireService service;

    @Test
    void getUserName() {
        when(inputOutputService.readFromInput()).thenReturn(USER_NAME);

        assertThat(service.getUserName()).isEqualTo(USER_NAME);

        verify(inputOutputService).writeToOutput("[CA] Hello, what is your name?");
    }

    @Test
    void interview() {
        when(inputOutputService.readFromInput()).thenReturn("1", "2", "1");

        service.interview(USER_NAME);

        verify(inputOutputService).writeToOutput("Question1 [CA] Answers: 1) answer1 2) answer2 3) answer3");
        verify(inputOutputService).writeToOutput("Question2 [CA] Answers: 1) answer1 2) answer2 3) answer3");
        verify(inputOutputService).writeToOutput("Question3 [CA] Answers: 1) answer1 2) answer2 3) answer3");
        verify(inputOutputService).writeToOutput("[CA] Test failed. Marina Kosareva, your score is 2");

    }
}
