package ru.otus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.configuration.LocaleProperties;

@Service
@RequiredArgsConstructor
public class DefaultLocalizationService implements LocalizationService {

    private final MessageSource messageSource;
    private final LocaleProperties properties;

    @Override
    public String getMessage(String code) {
        return messageSource.getMessage(code, null, properties.getLocale());
    }

    @Override
    public String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args.clone(), properties.getLocale());
    }
}
