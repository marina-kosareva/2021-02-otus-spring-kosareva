package ru.otus.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.service.DefaultInputOutputService;
import ru.otus.service.InputOutputService;

import java.io.InputStream;
import java.io.PrintStream;

@Configuration
public class InputOutputConfiguration {

    @Bean
    InputOutputService defaultInputOutputService(@Value("#{ T(java.lang.System).in}") InputStream in,
                                                 @Value("#{ T(java.lang.System).out}") PrintStream out) {
        return new DefaultInputOutputService(in, out);
    }
}
