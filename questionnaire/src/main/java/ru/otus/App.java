package ru.otus;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.service.QuestionnaireService;

@Configuration
@ComponentScan
@PropertySource("classpath:/application.properties")
public class App {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(App.class);

        QuestionnaireService service = context.getBean(QuestionnaireService.class);

        service.interview();
    }
}
