package ru.otus.service;

public interface LocalizationService {

    String getMessage(String code);

    String getMessage(String code, Object... args);

}
