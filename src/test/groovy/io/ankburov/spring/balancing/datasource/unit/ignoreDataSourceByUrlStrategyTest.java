package io.ankburov.spring.balancing.datasource.unit;

import io.ankburov.spring.balancing.datasource.ignore.IgnoreDataSourceByUrlStrategy;
import io.ankburov.spring.balancing.datasource.ignore.IgnoreDataSourceStrategy;
import io.ankburov.spring.balancing.datasource.property.ExtendedDataSourceProperties;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ignoreDataSourceByUrlStrategyTest {

    @Test
    void testIgnoreDataSource() {
        ExtendedDataSourceProperties dataSourceProperties = new ExtendedDataSourceProperties();
        dataSourceProperties.setUrl(IgnoreDataSourceStrategy.IGNORE);

        IgnoreDataSourceStrategy ignoreDataSourceStrategy = new IgnoreDataSourceByUrlStrategy();
        boolean isIgnore = ignoreDataSourceStrategy.ignore(dataSourceProperties);

        assertTrue(isIgnore);
    }

    @Test
    void testDoNotIgnoreDataSource() {
        ExtendedDataSourceProperties dataSourceProperties = new ExtendedDataSourceProperties();
        dataSourceProperties.setUrl("jdbc:oracle:thin:@localhost:1521/orclpdb1");

        IgnoreDataSourceStrategy ignoreDataSourceStrategy = new IgnoreDataSourceByUrlStrategy();
        boolean isIgnore = ignoreDataSourceStrategy.ignore(dataSourceProperties);

        assertFalse(isIgnore);
    }
}
