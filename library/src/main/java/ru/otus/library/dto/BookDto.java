package ru.otus.library.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private String id;
    private String title;
    private String genreTitle;
    private String authorFullName;
}
