package ru.otus.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultLocalizationServiceTest {

    private static final String LOCALE_LANGUAGE_EN = "en";
    private static final String LOCALE_COUNTRY_CA = "CA";

    @Mock
    private MessageSource messageSource;

    @Test
    void getMessage() {
        LocalizationService service = new DefaultLocalizationService(messageSource, LOCALE_LANGUAGE_EN, LOCALE_COUNTRY_CA);

        when(messageSource.getMessage("message", null, Locale.CANADA))
                .thenReturn("localizedMessage");

        assertThat(service.getMessage("message")).isEqualTo("localizedMessage");

        verify(messageSource).getMessage("message", null, Locale.CANADA);

    }

    @Test
    void getMessageWithParams() {
        LocalizationService service = new DefaultLocalizationService(messageSource, LOCALE_LANGUAGE_EN, LOCALE_COUNTRY_CA);

        String[] params = new String[]{"abc", "def"};

        when(messageSource.getMessage("message", params, Locale.CANADA))
                .thenReturn("localizedMessage");

        assertThat(service.getMessage("message", "abc", "def")).isEqualTo("localizedMessage");

        verify(messageSource).getMessage("message", params, Locale.CANADA);
    }
}
