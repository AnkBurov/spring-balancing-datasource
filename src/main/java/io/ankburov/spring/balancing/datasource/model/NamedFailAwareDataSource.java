package io.ankburov.spring.balancing.datasource.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import javax.sql.DataSource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class NamedFailAwareDataSource implements DataSource {

    private final String name;

    @Delegate
    private final DataSource dataSource;

    private volatile LocalDateTime lastFailed = null;

    // race condition doesn't matter here - doesn't matter which thread will update the field by his version of LocalDateTime.now()
    public void setFailed() {
        lastFailed = LocalDateTime.now();
    }

    public void setNotFailed() {
        if (lastFailed != null) {
            lastFailed = null;
        }
    }

    public boolean hasFailedOldEnoughOrOk(Duration timeThreshold) {
        return Optional.ofNullable(lastFailed)
                       .map(failed -> Duration.between(failed, LocalDateTime.now()))
                       .map(timeDifference -> timeDifference.compareTo(timeThreshold) >= 0)
                       .orElse(true);
    }
}
