package ru.otus.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.service.DefaultInputOutputService;
import ru.otus.service.InputOutputService;

@Configuration
public class InputOutputConfiguration {

    @Bean
    InputOutputService defaultInputOutputService() {
        return new DefaultInputOutputService(System.in, System.out);
    }
}
