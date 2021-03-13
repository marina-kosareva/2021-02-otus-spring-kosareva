package ru.otus.configuration;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.Map;

@Data
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "questions")
public class QuestionProperties {

    private final LocaleProperties localeProperties;

    private Map<Locale, String> csvFileNames;
    private int size;
    private int threshold;

    public String getFileName() {
        return csvFileNames.get(localeProperties.getLocale());
    }
}
