package ru.otus.dao;

import ru.otus.model.Question;

import java.io.IOException;
import java.util.List;

public interface Parser {

    List<Question> parse(String path) throws IOException;

}
