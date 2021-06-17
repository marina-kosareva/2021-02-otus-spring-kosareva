package ru.otus.batch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.NonNull;
import ru.otus.batch.entities.BookEntity;
import ru.otus.batch.model.BookDocument;
import ru.otus.batch.service.BookService;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public JpaPagingItemReader<BookEntity> bookItemReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<BookEntity>()
                .name("bookItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select b from BookEntity b "
                        + " left join fetch b.genre "
                        + " left join fetch b.author ")
                .pageSize(10)
                .saveState(true)
                .build();
    }

    @Bean
    public ItemProcessor<BookEntity, BookDocument> bookProcessor(BookService bookService) {
        return bookService::convert;
    }

    @Bean
    public MongoItemWriter<BookDocument> bookItemWriter(MongoTemplate mongoTemplate) {
        MongoItemWriter<BookDocument> mongoItemWriter = new MongoItemWriter<>();
        mongoItemWriter.setCollection("books");
        mongoItemWriter.setTemplate(mongoTemplate);
        return mongoItemWriter;
    }

    @Bean
    public Job importBooksJob(JobBuilderFactory jobBuilderFactory, Step transformBooksStep) {
        return jobBuilderFactory.get("importBooksJob")
                .incrementer(new RunIdIncrementer())
                .flow(transformBooksStep)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        log.info("beforeJob");
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        log.info("afterJob");
                    }
                })
                .build();
    }

    @Bean
    public Step transformBooksStep(StepBuilderFactory stepBuilderFactory,
                                   ItemReader<BookEntity> reader,
                                   ItemWriter<BookDocument> writer,
                                   ItemProcessor<BookEntity, BookDocument> processor) {
        return stepBuilderFactory.get("transformBooksStep")
                .<BookEntity, BookDocument>chunk(5)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(new ItemReadListener<>() {
                    public void beforeRead() {
                        log.info("beforeRead");
                    }

                    public void afterRead(@NonNull BookEntity entity) {
                        log.info("afterRead");
                    }

                    public void onReadError(@NonNull Exception exception) {
                        log.info("onReadError");
                    }
                })
                .listener(new ItemWriteListener<>() {
                    public void beforeWrite(@NonNull List list) {
                        log.info("beforeWrite");
                    }

                    public void afterWrite(@NonNull List list) {
                        log.info("afterWrite");
                    }

                    public void onWriteError(@NonNull Exception exception, @NonNull List list) {
                        log.info("onWriteError");
                    }
                })
                .listener(new ItemProcessListener<>() {
                    public void beforeProcess(@NonNull BookEntity entity) {
                        log.info("beforeProcess");
                    }

                    public void afterProcess(@NonNull BookEntity entity, BookDocument document) {
                        log.info("afterProcess");
                    }

                    public void onProcessError(@NonNull BookEntity entity, @NonNull Exception exception) {
                        log.info("onProcessError");
                    }
                })
                .listener(new ChunkListener() {
                    public void beforeChunk(@NonNull ChunkContext context) {
                        log.info("beforeChunk");
                    }

                    public void afterChunk(@NonNull ChunkContext context) {
                        log.info("afterChunk");
                    }

                    public void afterChunkError(ChunkContext context) {
                        log.info("afterChunkError");
                    }
                })
                .build();
    }
}
