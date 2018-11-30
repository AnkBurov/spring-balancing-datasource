package io.ankburov.spring.balancing.datasource.unit;

import io.ankburov.spring.balancing.datasource.balancingtype.MostHealthyFirstBalancingStrategy;
import io.ankburov.spring.balancing.datasource.model.NamedFailAwareDataSource;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MostHealthyFirstBalancingStrategyTest {

    @Test
    void testCorrectOrder() throws InterruptedException {
        val first = new NamedFailAwareDataSource("first", new SimpleDriverDataSource());
        val second = new NamedFailAwareDataSource("second", new SimpleDriverDataSource());
        second.setFailed();
        TimeUnit.MILLISECONDS.sleep(10);
        val third = new NamedFailAwareDataSource("third", new SimpleDriverDataSource());
        third.setFailed();

        List<NamedFailAwareDataSource> dataSources = Arrays.asList(third, second, first);

        List<NamedFailAwareDataSource> balancedDataSources = new MostHealthyFirstBalancingStrategy().apply(dataSources);

        assertEquals(first.getName(), balancedDataSources.get(0).getName());
        assertEquals(second.getName(), balancedDataSources.get(1).getName());
        assertEquals(third.getName(), balancedDataSources.get(2).getName());
    }
}
