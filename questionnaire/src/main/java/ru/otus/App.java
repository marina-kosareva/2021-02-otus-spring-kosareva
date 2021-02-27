package ru.otus;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.service.QuestionnaireService;

public class App {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("spring-context.xml");

        QuestionnaireService service = context.getBean(QuestionnaireService.class);
        service.questionsToDisplay().forEach(System.out::println);
    }
}
