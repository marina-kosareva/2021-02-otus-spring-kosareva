package ru.otus.reactive.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String genre;
    @NotBlank
    private String author;
}
