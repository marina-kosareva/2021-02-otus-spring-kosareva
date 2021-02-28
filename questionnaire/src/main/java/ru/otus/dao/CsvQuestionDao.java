package ru.otus.dao;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.StrMinMax;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.dozer.CsvDozerBeanReader;
import org.supercsv.io.dozer.ICsvDozerBeanReader;
import org.supercsv.prefs.CsvPreference;
import ru.otus.exceptions.CsvParseException;
import ru.otus.model.Question;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

@Slf4j
public class CsvQuestionDao implements QuestionDao {

    @Setter
    private String fileName;
    private static final String[] MAPPING = new String[]{
            "id",
            "title",
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
            new ParseInt(),
            new ParseInt(),
            new Optional(new StrMinMax(0, 100)),
            new ParseInt(),
            new Optional(new StrMinMax(0, 100)),
            new ParseInt(),
            new Optional(new StrMinMax(0, 100))
    };

    @Override
    public List<Question> getQuestions() throws CsvParseException {
        File file = getFileFromResource(fileName);
        List<Question> questions = new ArrayList<>();
        try (ICsvDozerBeanReader pojoReader = new CsvDozerBeanReader(new FileReader(file),
                CsvPreference.STANDARD_PREFERENCE)) {
            pojoReader.getHeader(true);
            pojoReader.configureBeanMapping(Question.class, MAPPING);
            Question question;
            while ((question = pojoReader.read(Question.class, PROCESSORS)) != null) {
                questions.add(question);
            }
        } catch (Exception ex) {
            throw new CsvParseException(ex.getMessage());
        }
        return questions;
    }

    private File getFileFromResource(String fileName) throws CsvParseException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new CsvParseException(format("File with name %s is not found", fileName));
        }
        return new File(resource.getFile());
    }
}
