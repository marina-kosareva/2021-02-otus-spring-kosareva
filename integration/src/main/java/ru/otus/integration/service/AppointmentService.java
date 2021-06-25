package ru.otus.integration.service;

import ru.otus.integration.domain.Appointment;
import ru.otus.integration.domain.AppointmentRequest;

import java.util.List;

public interface AppointmentService {

    List<Appointment> getPossibleAppointments(AppointmentRequest request);
}
