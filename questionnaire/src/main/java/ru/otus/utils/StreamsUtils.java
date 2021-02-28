package ru.otus.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;

@Slf4j
public class StreamsUtils {

    public static void writeToOutput(String str, Writer writer) {
        try {
            writer.write(str);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
