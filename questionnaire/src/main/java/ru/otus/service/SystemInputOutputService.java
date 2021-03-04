package ru.otus.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

@Slf4j
@Service
public class SystemInputOutputService implements InputOutputService {

    private Writer writer;
    private BufferedReader reader;

    @Autowired
    public SystemInputOutputService() {
        this.writer = new OutputStreamWriter(System.out);
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void writeToOutput(String str) {
        try {
            writer.write(str);
            writer.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public String readFromInput() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public void closeWriter() {
        try {
            writer.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void closeReader() {
        try {
            reader.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
