package ru.otus.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class DefaultLocalizationServiceTest {

    @MockBean
    private MessageSource messageSource;

    @Autowired
    DefaultLocalizationService service;

    @Test
    void getMessage() {

        when(messageSource.getMessage("message", new Object[0], Locale.CANADA))
                .thenReturn("localizedMessage");

        assertThat(service.getMessage("message")).isEqualTo("localizedMessage");

        verify(messageSource).getMessage("message", new Object[0], Locale.CANADA);

    }

    @Test
    void getMessageWithParams() {

        String[] params = new String[]{"abc", "def"};

        when(messageSource.getMessage("message", params, Locale.CANADA))
                .thenReturn("localizedMessage");

        assertThat(service.getMessage("message", "abc", "def")).isEqualTo("localizedMessage");

        verify(messageSource).getMessage("message", params, Locale.CANADA);
    }
}
