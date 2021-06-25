package ru.otus.integration.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.integration.BeautySalon;
import ru.otus.integration.domain.AppointmentRequest;
import ru.otus.integration.domain.AppointmentResponse;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class AppointmentController {

    private final BeautySalon beautySalon;

    @GetMapping("/appointment")
    public AppointmentResponse getPossibleAppointments(@RequestBody @Valid List<AppointmentRequest> request) {
        return beautySalon.getPossibleAppointments(request);
    }
}
