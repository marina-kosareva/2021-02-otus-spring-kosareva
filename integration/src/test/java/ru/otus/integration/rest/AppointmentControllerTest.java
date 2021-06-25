package ru.otus.integration.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.integration.domain.Appointment;
import ru.otus.integration.domain.AppointmentRequest;
import ru.otus.integration.domain.AppointmentResponse;
import ru.otus.integration.domain.ServiceType;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Arrays.asList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AppointmentControllerTest {

    private static final LocalDateTime DATE = LocalDateTime.parse("2021-06-25T00:00:00");

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void getPossibleAppointments() throws Exception {
        List<AppointmentRequest> requestList = asList(
                AppointmentRequest.builder()
                        .serviceType(ServiceType.MANICURE)
                        .desirableImplementer("Tatyana")
                        .startDesirableTime(DATE)
                        .endDesirableTime(DATE.plusHours(24))
                        .build(),
                AppointmentRequest.builder()
                        .serviceType(ServiceType.PEDICURE)
                        .startDesirableTime(DATE)
                        .endDesirableTime(LocalDateTime.now().plusHours(24))
                        .build(),
                AppointmentRequest.builder()
                        .serviceType(ServiceType.HAIRCUT)
                        .desirableImplementer("Olesya")
                        .startDesirableTime(DATE)
                        .endDesirableTime(DATE.plusHours(24))
                        .build());
        AppointmentResponse expected = AppointmentResponse.builder()
                .proposals(asList(
                        Appointment.builder()
                                .serviceType(ServiceType.MANICURE)
                                .implementer("Tatyana")
                                .startTime(DATE)
                                .endTime(DATE.plusHours(2))
                                .build(),
                        Appointment.builder()
                                .serviceType(ServiceType.MANICURE)
                                .implementer("Ludmila")
                                .startTime(DATE)
                                .endTime(DATE.plusHours(2))
                                .build(),
                        Appointment.builder()
                                .serviceType(ServiceType.PEDICURE)
                                .implementer("Nail master on duty")
                                .startTime(DATE)
                                .endTime(DATE.plusHours(2))
                                .build(),
                        Appointment.builder()
                                .serviceType(ServiceType.PEDICURE)
                                .implementer("Ludmila")
                                .startTime(DATE)
                                .endTime(DATE.plusHours(2))
                                .build(),
                        Appointment.builder()
                                .serviceType(ServiceType.HAIRCUT)
                                .implementer("Olesya")
                                .startTime(DATE)
                                .endTime(DATE.plusHours(2))
                                .build(),
                        Appointment.builder()
                                .serviceType(ServiceType.HAIRCUT)
                                .implementer("Oksana")
                                .startTime(DATE)
                                .endTime(DATE.plusHours(2))
                                .build()
                ))
                .build();
        mvc.perform(get("/appointment")
                .contentType(APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(requestList)))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(expected)));
    }
}
