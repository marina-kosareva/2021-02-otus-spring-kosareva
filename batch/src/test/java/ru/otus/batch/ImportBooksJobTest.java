package ru.otus.batch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.otus.batch.model.AuthorDocument;
import ru.otus.batch.model.BookDocument;
import ru.otus.batch.model.GenreDocument;
import ru.otus.batch.repository.AuthorRepository;
import ru.otus.batch.repository.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@SpringBatchTest
class ImportBooksJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private GenreRepository genreRepository;

    @BeforeEach
    void clearMetaData() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void testJob() throws Exception {
        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo("importBooksJob");
        assertThat(mongoOperations.findAll(AuthorDocument.class)).asList().isEmpty();
        assertThat(mongoOperations.findAll(GenreDocument.class)).asList().isEmpty();
        assertThat(mongoOperations.findAll(BookDocument.class)).asList().isEmpty();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParametersBuilder()
                .toJobParameters());
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        AuthorDocument expectedAuthor1 = authorRepository.findByFirstNameAndLastName("first_name_1", "last_name_1")
                .orElse(null);
        AuthorDocument expectedAuthor2 = authorRepository.findByFirstNameAndLastName("first_name_2", "last_name_2")
                .orElse(null);
        assertThat(expectedAuthor1).isNotNull();
        assertThat(expectedAuthor2).isNotNull();

        assertThat(mongoOperations.findAll(AuthorDocument.class))
                .asList()
                .containsExactlyInAnyOrder(expectedAuthor1, expectedAuthor2);

        GenreDocument expectedGenre1 = genreRepository.findByTitle("title_1").orElse(null);
        GenreDocument expectedGenre2 = genreRepository.findByTitle("title_2").orElse(null);
        assertThat(expectedGenre1).isNotNull();
        assertThat(expectedGenre2).isNotNull();

        assertThat(mongoOperations.findAll(GenreDocument.class))
                .asList()
                .containsExactlyInAnyOrder(expectedGenre1, expectedGenre2);

        BookDocument expectedBook1 = BookDocument.builder()
                .title("title_1")
                .genre(expectedGenre2)
                .author(expectedAuthor1)
                .build();
        BookDocument expectedBook2 = BookDocument.builder()
                .title("title_2")
                .genre(expectedGenre1)
                .author(expectedAuthor2)
                .build();
        BookDocument expectedBook3 = BookDocument.builder()
                .title("title_3")
                .genre(expectedGenre1)
                .author(expectedAuthor1)
                .build();
        BookDocument expectedBook4 = BookDocument.builder()
                .title("title_4")
                .genre(expectedGenre2)
                .author(expectedAuthor2)
                .build();
        assertThat(actualBooksWithNullId())
                .asList()
                .containsExactlyInAnyOrder(expectedBook1, expectedBook2, expectedBook3, expectedBook4);

    }

    private List<BookDocument> actualBooksWithNullId() {
        return mongoOperations.findAll(BookDocument.class).stream()
                .peek(document -> document.setId(null))
                .collect(Collectors.toList());
    }
}
