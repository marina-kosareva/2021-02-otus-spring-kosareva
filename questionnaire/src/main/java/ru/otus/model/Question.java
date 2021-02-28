package ru.otus.model;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class Question {
    private int id;
    private String title;
    private int correctAnswer;
    private List<Answer> answers;

    @Override
    public String toString() {
        return String.format("%n%s Answers: %s",
                title,
                answers.stream()
                        .map(Answer::toString)
                        .collect(Collectors.joining(" ")));
    }

}
