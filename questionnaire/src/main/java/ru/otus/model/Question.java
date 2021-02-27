package ru.otus.model;

import lombok.Data;

import java.util.List;

@Data
public class Question {
    private int id;
    private String title;
    private QuestionType type;
    private int correctAnswer;
    private List<Answer> answers;
}
