package ru.otus.reactive.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import ru.otus.reactive.model.Book;

@ChangeLog
public class DatabaseChangelog {
    @ChangeSet(order = "001", id = "insertData", author = "kosmar", runAlways = true)
    public void insertData(MongockTemplate template) {
        template.dropCollection(Book.class);
        template.save(Book.builder()
                .title("Zuleikha opens her eyes")
                .author("Guzel Yahina")
                .genre("Historical fiction")
                .build());
        template.save(Book.builder()
                .title("Clean code")
                .author("Robert Martin")
                .genre("Academic")
                .build());
    }
}
