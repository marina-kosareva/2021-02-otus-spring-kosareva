package ru.otus.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.exceptions.ReadException;
import ru.otus.exceptions.WriteException;

import java.io.*;

@Slf4j
@Service
public class DefaultInputOutputService implements InputOutputService {

    public void writeToOutput(OutputStream outputStream, String str) throws WriteException {
        Writer writer = new OutputStreamWriter(outputStream);
        try {
            writer.write(str);
            writer.flush();
        } catch (IOException e) {
            throw new WriteException("Exception during writing to output", e);
        }
    }

    public String readFromInput(InputStream inputStream) throws ReadException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new ReadException("Exception during reading from input", e);
        }
    }
}
