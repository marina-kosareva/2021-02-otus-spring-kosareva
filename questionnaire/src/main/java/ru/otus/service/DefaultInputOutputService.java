package ru.otus.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.exceptions.ReadException;
import ru.otus.exceptions.WriteException;

import java.io.*;

@Slf4j
@Service
public class DefaultInputOutputService implements InputOutputService {

    private final Writer writer;
    private final BufferedReader reader;

    public DefaultInputOutputService(InputStream in, OutputStream out) {
        this.writer = new OutputStreamWriter(out);
        this.reader = new BufferedReader(new InputStreamReader(in));
    }

    public void writeToOutput(String str) throws WriteException {
        try {
            writer.write(str);
            writer.flush();
        } catch (IOException e) {
            throw new WriteException("Exception during writing to output", e);
        }
    }

    public String readFromInput() throws ReadException {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new ReadException("Exception during reading from input", e);
        }
    }
}
