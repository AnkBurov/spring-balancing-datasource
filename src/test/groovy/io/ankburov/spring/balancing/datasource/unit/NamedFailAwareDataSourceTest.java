package io.ankburov.spring.balancing.datasource.unit;

import io.ankburov.spring.balancing.datasource.model.NamedFailAwareDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NamedFailAwareDataSourceTest {

    @Test
    public void testHasFailedOldEnough() throws InterruptedException {
        NamedFailAwareDataSource failedDataSource = new NamedFailAwareDataSource("failed", new SimpleDriverDataSource());
        failedDataSource.setFailed();
        Duration timeThreshold = Duration.ofMillis(100);

        TimeUnit.MILLISECONDS.sleep(200);

        boolean hasFailedOldEnoughOrOk = failedDataSource.hasFailedOldEnoughOrOk(timeThreshold);

        assertTrue(hasFailedOldEnoughOrOk);
    }

    @Test
    public void testNotFailed() {
        NamedFailAwareDataSource dataSource = new NamedFailAwareDataSource("first", new SimpleDriverDataSource());
        Duration timeThreshold = Duration.ofNanos(0);

        boolean hasFailedOldEnoughOrOk = dataSource.hasFailedOldEnoughOrOk(timeThreshold);

        assertTrue(hasFailedOldEnoughOrOk);
    }

    @Test
    public void testHasFailedNotOldEnough() throws InterruptedException {
        NamedFailAwareDataSource failedDataSource = new NamedFailAwareDataSource("failed", new SimpleDriverDataSource());
        failedDataSource.setFailed();
        Duration timeThreshold = Duration.ofMillis(200);

        TimeUnit.MILLISECONDS.sleep(100);

        boolean hasFailedOldEnoughOrOk = failedDataSource.hasFailedOldEnoughOrOk(timeThreshold);

        assertFalse(hasFailedOldEnoughOrOk);
    }
}
