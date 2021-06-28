package ru.otus.library.health;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.library.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class LibraryUserHealthIndicator implements HealthIndicator {

    private final UserRepository userRepository;

    @Override
    public Health health() {
        long count = userRepository.count();
        if (count == 0) {
            return Health
                    .down()
                    .withDetail("message", "no users")
                    .build();
        }
        return Health
                .up()
                .withDetail("message", "count of users: " + count)
                .build();
    }
}
