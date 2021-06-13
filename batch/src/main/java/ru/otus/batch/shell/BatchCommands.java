package ru.otus.batch.shell;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class BatchCommands {

    private final Job importBooksJob;
    private final JobLauncher jobLauncher;

    @ShellMethod(value = "startImportBooksJob", key = "start")
    public void startImportBooksJob() throws Exception {
        jobLauncher.run(importBooksJob, new JobParametersBuilder().toJobParameters());
    }
}
