package io.ankburov.spring.balancing.datasource.unit;

import io.ankburov.spring.balancing.datasource.filter.FilteringStrategy;
import io.ankburov.spring.balancing.datasource.filter.OnlyWorkingFilteringStrategy;
import io.ankburov.spring.balancing.datasource.model.NamedFailAwareDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OnlyWorkingFilteringStrategyTest {

    @Test
    public void testFiltered() {
        NamedFailAwareDataSource firstDataSource = new NamedFailAwareDataSource("first", new SimpleDriverDataSource());
        NamedFailAwareDataSource secondDataSource = new NamedFailAwareDataSource("secondFailed", new SimpleDriverDataSource());
        secondDataSource.setFailed();

        List<NamedFailAwareDataSource> dataSources = Arrays.asList(firstDataSource, secondDataSource);

        Duration timeThreshold = Duration.ofHours(1);
        FilteringStrategy filteringStrategy = new OnlyWorkingFilteringStrategy(timeThreshold);
        List<NamedFailAwareDataSource> filteredDataSources = filteringStrategy.filter(dataSources);

        assertEquals(1, filteredDataSources.size());
        assertEquals(firstDataSource.getName(), filteredDataSources.get(0).getName());
    }

    @Test
    public void testAllFiltered() {
        NamedFailAwareDataSource firstDataSource = new NamedFailAwareDataSource("first", new SimpleDriverDataSource());
        firstDataSource.setFailed();
        NamedFailAwareDataSource secondDataSource = new NamedFailAwareDataSource("secondFailed", new SimpleDriverDataSource());
        secondDataSource.setFailed();

        List<NamedFailAwareDataSource> dataSources = Arrays.asList(firstDataSource, secondDataSource);

        Duration timeThreshold = Duration.ofHours(1);
        FilteringStrategy filteringStrategy = new OnlyWorkingFilteringStrategy(timeThreshold);
        List<NamedFailAwareDataSource> filteredDataSources = filteringStrategy.filter(dataSources);

        assertEquals(dataSources, filteredDataSources);
    }
}
