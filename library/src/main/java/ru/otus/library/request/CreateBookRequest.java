package ru.otus.library.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateBookRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String genreId;
    @NotBlank
    private String authorId;
}
