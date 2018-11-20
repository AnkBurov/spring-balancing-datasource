package io.ankburov.spring.balancing.datasource.unit;

import io.ankburov.spring.balancing.datasource.failed.AlwaysUpdateFailedDataSourceStrategy;
import io.ankburov.spring.balancing.datasource.model.NamedFailAwareDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AlwaysUpdateFailedDataSourceStrategyTest {

    @Test
    public void testFailed() {
        NamedFailAwareDataSource failedDataSource = new NamedFailAwareDataSource("failed", new SimpleDriverDataSource());
        assertNull(failedDataSource.getLastFailed());

        new AlwaysUpdateFailedDataSourceStrategy().update(failedDataSource);

        assertNotNull(failedDataSource.getLastFailed());
    }
}
