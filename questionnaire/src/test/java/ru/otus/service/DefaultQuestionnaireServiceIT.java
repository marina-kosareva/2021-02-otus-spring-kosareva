package ru.otus.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.otus.App;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(App.class)
@TestPropertySource("classpath:/application-test.properties")
class DefaultQuestionnaireServiceIT {

    @MockBean
    private InputOutputService inputOutputService;
    @Autowired
    private QuestionnaireService service;

    @Test
    void interview() {
        when(inputOutputService.readFromInput(System.in)).thenReturn("Marina Kosareva", "1", "2", "1");

        service.interview();

        verify(inputOutputService).writeToOutput(System.out, "Hello, what is your name?");
        verify(inputOutputService).writeToOutput(System.out, "Question1 Answers: 1) answer1 2) answer2 3) answer3");
        verify(inputOutputService).writeToOutput(System.out, "Question2 Answers: 1) answer1 2) answer2 3) answer3");
        verify(inputOutputService).writeToOutput(System.out, "Question3 Answers: 1) answer1 2) answer2 3) answer3");
        verify(inputOutputService).writeToOutput(System.out, "Test failed. Marina Kosareva, your score is 2");

    }
}
