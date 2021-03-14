package ru.otus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultPrintService implements PrintService {

    private final InputOutputService inputOutputService;
    private final LocalizationService localizationService;

    @Override
    public void printLocalizedMessage(String messageCode, Object... args) {
        String localizedMessage = localizationService.getMessage(messageCode, args);
        printMessage(localizedMessage);
    }

    @Override
    public void printMessage(String message) {
        inputOutputService.writeToOutput(message);
    }
}
