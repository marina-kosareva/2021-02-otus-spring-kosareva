package ru.otus.integration.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Builder
@EqualsAndHashCode
public class AppointmentRequest {

    @NotNull
    private final ServiceType serviceType;
    private final String desirableImplementer;
    @NotNull
    private final LocalDateTime startDesirableTime;
    @NotNull
    private final LocalDateTime endDesirableTime;

    public boolean isNails() {
        return serviceType.equals(ServiceType.MANICURE)
                || serviceType.equals(ServiceType.PEDICURE);
    }
}
