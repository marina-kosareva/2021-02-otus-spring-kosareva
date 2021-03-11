package ru.otus.dao;

import ru.otus.exceptions.QuestionsLoadingException;
import ru.otus.model.Question;

import java.util.List;

public interface QuestionDao {

    List<Question> getQuestions() throws QuestionsLoadingException;

}
