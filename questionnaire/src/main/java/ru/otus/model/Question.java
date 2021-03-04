package ru.otus.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private int id;
    private String title;
    private int correctAnswer;
    private List<Answer> answers;

    @Override
    public String toString() {
        return String.format("%s Answers: %s",
                title,
                answers.stream()
                        .map(Answer::toString)
                        .collect(Collectors.joining(" ")));
    }
}
