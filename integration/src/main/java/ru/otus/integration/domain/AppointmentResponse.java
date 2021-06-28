package ru.otus.integration.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
public class AppointmentResponse {
    private final List<Appointment> proposals;
}
