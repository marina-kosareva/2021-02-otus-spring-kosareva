package ru.otus.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.service.QuestionnaireService;

import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class QuestionnaireCommands {

    private final QuestionnaireService service;

    private String userName;

    @ShellMethod(value = "Login command", key = "login")
    public String login() {
        userName = service.getUserName();
        return "Enter 'start' to start interview";
    }

    @ShellMethod(value = "Start interview", key = {"start"})
    @ShellMethodAvailability(value = "isInterviewCommandAvailable")
    public void interview() {
        service.interview(userName);
    }

    private Availability isInterviewCommandAvailable() {
        return Optional.ofNullable(userName)
                .map(name -> Availability.available())
                .orElse(Availability.unavailable("Please login first"));
    }

}
