package ru.otus.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class DefaultLocalizationService implements LocalizationService {

    private final MessageSource messageSource;
    private final String language;
    private final String country;

    public DefaultLocalizationService(MessageSource messageSource,
                                      @Value("${locale.language:en}") String language,
                                      @Value("${locale.country:US}") String country) {
        this.messageSource = messageSource;
        this.language = language;
        this.country = country;
    }

    @Override
    public String getMessage(String code) {
        return messageSource.getMessage(code, null, new Locale(language, country));
    }

    @Override
    public String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args.clone(), new Locale(language, country));
    }
}
