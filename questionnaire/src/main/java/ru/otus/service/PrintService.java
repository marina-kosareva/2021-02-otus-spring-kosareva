package ru.otus.service;

public interface PrintService {

    void printLocalizedMessage(String code, Object... args);

    void printMessage(String message);

}
