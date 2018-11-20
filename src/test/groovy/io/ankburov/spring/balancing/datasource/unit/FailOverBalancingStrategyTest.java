package io.ankburov.spring.balancing.datasource.unit;

import io.ankburov.spring.balancing.datasource.balancingtype.BalancingStrategy;
import io.ankburov.spring.balancing.datasource.balancingtype.FailOverBalancingStrategy;
import io.ankburov.spring.balancing.datasource.model.NamedFailAwareDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FailOverBalancingStrategyTest {

    @Test
    public void testApply() {
        NamedFailAwareDataSource firstDataSource = new NamedFailAwareDataSource("first", new SimpleDriverDataSource());
        List<NamedFailAwareDataSource> dataSources = Collections.singletonList(firstDataSource);

        BalancingStrategy balancingStrategy = new FailOverBalancingStrategy();
        List<NamedFailAwareDataSource> appliedDataSources = balancingStrategy.apply(dataSources);

        assertEquals(dataSources, appliedDataSources);
    }
}
