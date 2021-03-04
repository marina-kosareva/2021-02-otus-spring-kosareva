package ru.otus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.dao.QuestionDao;
import ru.otus.exceptions.QuestionsLoadingException;
import ru.otus.model.Question;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultQuestionService implements QuestionService {

    private final QuestionDao dao;

    @Override
    public List<Question> getQuestions() throws QuestionsLoadingException {
        return dao.getQuestions();
    }

}
