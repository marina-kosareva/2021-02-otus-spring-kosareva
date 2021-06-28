package ru.otus.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.integration.domain.AppointmentRequest;
import ru.otus.integration.domain.AppointmentResponse;

import java.util.List;

@MessagingGateway
public interface BeautySalon {

    @Gateway(requestChannel = "appointmentRequestsChannel",
            replyChannel = "appointmentResponseChannel")
    AppointmentResponse getPossibleAppointments(List<AppointmentRequest> requests);
}
