package ru.otus.integration.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@EqualsAndHashCode
public class Appointment {

    private final ServiceType serviceType;
    private final String implementer;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

}
