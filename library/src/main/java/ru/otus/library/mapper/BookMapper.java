package ru.otus.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.otus.library.dto.BookDto;
import ru.otus.library.model.Book;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE, componentModel = "spring")
public interface BookMapper {
    @Mappings({
            @Mapping(target = "genreTitle", source = "genre.title"),
            @Mapping(target = "authorFullName",
                    expression = "java(book.getAuthor().getFirstName() + \" \" + book.getAuthor().getLastName())")
    })
    BookDto bookToBookDto(Book book);
}
