package ru.otus.integration.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.integration.domain.Appointment;
import ru.otus.integration.domain.AppointmentRequest;
import ru.otus.integration.domain.AppointmentResponse;
import ru.otus.integration.service.AppointmentService;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class IntegrationConfig {

    private static final String GET_POSSIBLE_APPOINTMENTS_METHOD = "getPossibleAppointments";

    @Bean
    public QueueChannel appointmentRequestsChannel() {
        return MessageChannels.queue(10).get();
    }

    @Bean
    public PublishSubscribeChannel appointmentResponseChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2).get();
    }

    @Bean
    public IntegrationFlow appointmentFlow(@Qualifier("hairAppointmentService") AppointmentService hairService,
                                           @Qualifier("nailAppointmentService") AppointmentService nailService) {
        return IntegrationFlows.from("appointmentRequestsChannel")
                .split()
                .<AppointmentRequest, Boolean>route(AppointmentRequest::isNails,
                        m -> m
                                .subFlowMapping(true,
                                        sf -> sf.gateway(f -> f.handle(nailService, GET_POSSIBLE_APPOINTMENTS_METHOD)))
                                .subFlowMapping(false,
                                        sf -> sf.gateway(f -> f.handle(hairService, GET_POSSIBLE_APPOINTMENTS_METHOD))))
                .aggregate()
                .transform(this::transformToAppointmentResponse)
                .channel("appointmentResponseChannel")
                .get();
    }

    private AppointmentResponse transformToAppointmentResponse(List<List<Appointment>> appointmentDtoList) {
        return AppointmentResponse.builder()
                .proposals(appointmentDtoList.stream()
                        .flatMap(List::stream)
                        .collect(Collectors.toList()))
                .build();
    }
}
