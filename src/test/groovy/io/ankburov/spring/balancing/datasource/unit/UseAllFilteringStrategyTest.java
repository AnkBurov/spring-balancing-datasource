package io.ankburov.spring.balancing.datasource.unit;

import io.ankburov.spring.balancing.datasource.filter.FilteringStrategy;
import io.ankburov.spring.balancing.datasource.filter.UseAllFilteringStrategy;
import io.ankburov.spring.balancing.datasource.model.NamedFailAwareDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UseAllFilteringStrategyTest {

    @Test
    public void testFilter() {
        NamedFailAwareDataSource firstDataSource = new NamedFailAwareDataSource("first", new SimpleDriverDataSource());
        NamedFailAwareDataSource secondDataSource = new NamedFailAwareDataSource("second", new SimpleDriverDataSource());
        List<NamedFailAwareDataSource> dataSources = Arrays.asList(firstDataSource, secondDataSource);

        FilteringStrategy filteringStrategy = new UseAllFilteringStrategy();
        List<NamedFailAwareDataSource> filteredDataSources = filteringStrategy.filter(dataSources);

        assertEquals(dataSources, filteredDataSources);
    }
}
