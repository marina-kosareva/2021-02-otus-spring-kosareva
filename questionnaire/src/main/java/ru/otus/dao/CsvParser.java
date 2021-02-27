package ru.otus.dao;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.StrMinMax;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.dozer.CsvDozerBeanReader;
import org.supercsv.io.dozer.ICsvDozerBeanReader;
import org.supercsv.prefs.CsvPreference;
import ru.otus.model.Question;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CsvParser implements Parser {
    private static final String[] MAPPING = new String[]{
            "id",
            "title",
            "type",
            "correctAnswer",
            "answers[0].id",
            "answers[0].title",
            "answers[1].id",
            "answers[1].title",
            "answers[2].id",
            "answers[2].title",

    };
    private static final CellProcessor[] PROCESSORS = new CellProcessor[]{
            new ParseInt(),
            new StrMinMax(0, 100),
            new StrMinMax(0, 100),
            new Optional(),
            new Optional(),
            new Optional(new StrMinMax(0, 100)),
            new Optional(),
            new Optional(new StrMinMax(0, 100)),
            new Optional(),
            new Optional(new StrMinMax(0, 100))
    };

    @Override
    public List<Question> parse(String path) throws IOException {
        String fileName = Objects.requireNonNull(getClass().getClassLoader().getResource(path)).getFile();
        List<Question> questions = new ArrayList<>();
        try (ICsvDozerBeanReader pojoReader = new CsvDozerBeanReader(new FileReader(new File(fileName)),
                CsvPreference.STANDARD_PREFERENCE)) {
            pojoReader.getHeader(true);
            pojoReader.configureBeanMapping(Question.class, MAPPING);
            Question question;
            while ((question = pojoReader.read(Question.class, PROCESSORS)) != null) {
                questions.add(question);
            }
        }
        return questions;
    }
}