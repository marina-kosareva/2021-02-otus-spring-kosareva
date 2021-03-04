package ru.otus.service;

public interface InputOutputService {

    void writeToOutput(String str);

    String readFromInput();

    void closeWriter();

    void closeReader();

}
