package ru.otus.service;

import ru.otus.exceptions.ReadException;
import ru.otus.exceptions.WriteException;

import java.io.InputStream;
import java.io.OutputStream;

public interface InputOutputService {

    void writeToOutput(OutputStream outputStream, String str) throws WriteException;

    String readFromInput(InputStream inputStream) throws ReadException;

}
