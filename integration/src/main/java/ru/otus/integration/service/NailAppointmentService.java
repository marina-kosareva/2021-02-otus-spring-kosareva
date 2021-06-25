package ru.otus.integration.service;

import org.springframework.stereotype.Service;
import ru.otus.integration.domain.Appointment;
import ru.otus.integration.domain.AppointmentRequest;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;

@Service
public class NailAppointmentService implements AppointmentService {

    @Override
    public List<Appointment> getPossibleAppointments(AppointmentRequest request) {
        // some business logic should be here...
        return asList(Appointment.builder()
                        .serviceType(request.getServiceType())
                        .implementer(ofNullable(request.getDesirableImplementer())
                                .orElse("Nail master on duty"))
                        .startTime(request.getStartDesirableTime())
                        .endTime(request.getStartDesirableTime().plusHours(2))
                        .build(),
                Appointment.builder()
                        .serviceType(request.getServiceType())
                        .implementer("Ludmila")
                        .startTime(request.getStartDesirableTime())
                        .endTime(request.getStartDesirableTime().plusHours(2))
                        .build()
        );
    }
}
