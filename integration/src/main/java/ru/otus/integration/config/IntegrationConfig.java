package ru.otus.integration.config;

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

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class IntegrationConfig {

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
    public IntegrationFlow appointmentFlow() {
        return IntegrationFlows.from("appointmentRequestsChannel")
                .split()
                .<AppointmentRequest, Boolean>route(AppointmentRequest::isNails,
                        m -> m
                                .subFlowMapping(true, sf -> sf.gateway(appointmentNailsFlow()))
                                .subFlowMapping(false, sf -> sf.gateway(appointmentHairFlow())))
                .aggregate()
                .transform(this::transformToAppointmentResponse)
                .channel("appointmentResponseChannel")
                .get();
    }

    @Bean
    public IntegrationFlow appointmentHairFlow() {
        return f -> f.handle("hairAppointmentService", "getPossibleAppointments");
    }

    @Bean
    public IntegrationFlow appointmentNailsFlow() {
        return f -> f.handle("nailAppointmentService", "getPossibleAppointments");
    }

    private AppointmentResponse transformToAppointmentResponse(List<List<Appointment>> appointmentDtoList) {
        return AppointmentResponse.builder()
                .proposals(appointmentDtoList.stream()
                        .flatMap(List::stream)
                        .collect(Collectors.toList()))
                .build();
    }
}
