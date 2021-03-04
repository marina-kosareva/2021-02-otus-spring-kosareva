package ru.otus.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.model.Question;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DefaultEvaluationServiceTest {

    @InjectMocks
    private DefaultEvaluationService service;

    @Test
    void evaluate_correctAnswer() {
        Question question = Question.builder()
                .correctAnswer(1)
                .build();
        assertThat(service.evaluate(question, "any answer")).isZero();
    }

    @Test
    void evaluate_incorrectAnswer() {

        Question question = Question.builder()
                .correctAnswer(1)
                .build();
        assertThat(service.evaluate(question, "1")).isEqualTo(1);
    }
}
