package ru.otus.service;

import lombok.RequiredArgsConstructor;
import ru.otus.dao.QuestionDao;
import ru.otus.exceptions.CsvParseException;
import ru.otus.model.Question;

import java.util.List;

@RequiredArgsConstructor
public class DefaultQuestionService implements QuestionService {

    private final QuestionDao dao;

    @Override
    public List<Question> getQuestions() throws CsvParseException {
        return dao.getQuestions();
    }

}
