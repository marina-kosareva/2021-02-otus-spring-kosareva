package ru.otus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DefaultPrintService implements PrintService {

    private final InputOutputService inputOutputService;
    private final LocalizationService localizationService;

    @Override
    public void printLocalizedMessage(String messageCode, Object... args) {
        String localizedMessage = Stream.of(args).count() != 0L
                ? localizationService.getMessage(messageCode, args)
                : localizationService.getMessage(messageCode);
        printMessage(localizedMessage);
    }

    @Override
    public void printMessage(String message) {
        inputOutputService.writeToOutput(message);
    }
}
