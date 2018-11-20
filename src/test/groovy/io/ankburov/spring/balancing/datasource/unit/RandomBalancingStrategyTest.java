package io.ankburov.spring.balancing.datasource.unit;

import io.ankburov.spring.balancing.datasource.balancingtype.BalancingStrategy;
import io.ankburov.spring.balancing.datasource.balancingtype.RandomBalancingStrategy;
import io.ankburov.spring.balancing.datasource.model.NamedFailAwareDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class RandomBalancingStrategyTest {

    @Test
    public void testRandomShuffle() {
        NamedFailAwareDataSource firstDataSource = new NamedFailAwareDataSource("first", new SimpleDriverDataSource());
        NamedFailAwareDataSource secondDataSource = new NamedFailAwareDataSource("second", new SimpleDriverDataSource());

        List<NamedFailAwareDataSource> dataSources = Arrays.asList(firstDataSource, secondDataSource);
        BalancingStrategy balancingStrategy = new RandomBalancingStrategy();

        boolean firstWasShuffled = false;
        boolean secondWasShuffled = false;

        for (int i = 0; i < 100; i++) {
            List<NamedFailAwareDataSource> shuffledDataSources = balancingStrategy.apply(dataSources);

            assertEquals(dataSources.size(), shuffledDataSources.size());

            if (shuffledDataSources.get(0).getName().equals(firstDataSource.getName())) {
                firstWasShuffled = true;
            } else if (shuffledDataSources.get(0).getName().equals(secondDataSource.getName())) {
                secondWasShuffled = true;
            }
            if (firstWasShuffled && secondWasShuffled) {
                return;
            }
        }

        fail("List wasn't shuffled");
    }
}
