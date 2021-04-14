package ru.otus.library.model;

import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@ToString
@Getter
public class Comment {
    private String uuid;
    private String text;

    public Comment(String text) {
        this.uuid = UUID.randomUUID().toString();
        this.text = text;
    }
}
