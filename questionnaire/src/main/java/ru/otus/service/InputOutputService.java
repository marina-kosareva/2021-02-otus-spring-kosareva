package ru.otus.service;

import ru.otus.exceptions.ReadException;
import ru.otus.exceptions.WriteException;

public interface InputOutputService {

    void writeToOutput(String str) throws WriteException;

    String readFromInput() throws ReadException;

}
