package io.ankburov.spring.balancing.datasource.unit;

import io.ankburov.spring.balancing.datasource.ignore.IgnoreDataSourceByUrlStrategy;
import io.ankburov.spring.balancing.datasource.ignore.IgnoreDataSourceStrategy;
import io.ankburov.spring.balancing.datasource.property.BalancingDataSourceProperties;
import io.ankburov.spring.balancing.datasource.property.ExtendedDataSourceProperties;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BalancingDataSourcePropertiesTest {

    private static final IgnoreDataSourceStrategy IGNORE_STRATEGY = new IgnoreDataSourceByUrlStrategy();

    @Test
    void testOrderCorrect() {
        BalancingDataSourceProperties properties = new BalancingDataSourceProperties();
        properties.setDatasources(new LinkedHashMap<>());
        properties.getDatasources().put("second", new ExtendedDataSourceProperties()); //incorrect order
        properties.getDatasources().put("first", new ExtendedDataSourceProperties());
        properties.setOrder("first, second");

        properties.makeSureDataSourceOrder(IGNORE_STRATEGY);

        val iterator = properties.getDatasources().keySet().iterator();
        properties.getDatasources().entrySet();
        assertEquals("first", iterator.next());
        assertEquals("second", iterator.next());
    }

    @Test
    void testEmptyOrder() {
        assertThrows(IllegalStateException.class, () -> {
            BalancingDataSourceProperties properties = new BalancingDataSourceProperties();
            properties.setDatasources(new LinkedHashMap<>());
            properties.getDatasources().put("second", new ExtendedDataSourceProperties());
            properties.getDatasources().put("first", new ExtendedDataSourceProperties());

            properties.makeSureDataSourceOrder(IGNORE_STRATEGY);
        });
    }

    @Test
    void testOrderIllegalSize() {
        assertThrows(IllegalStateException.class, () -> {
            BalancingDataSourceProperties properties = new BalancingDataSourceProperties();
            properties.setDatasources(new LinkedHashMap<>());
            properties.getDatasources().put("second", new ExtendedDataSourceProperties()); //incorrect order
            properties.getDatasources().put("first", new ExtendedDataSourceProperties());
            properties.setOrder("first");

            properties.makeSureDataSourceOrder(IGNORE_STRATEGY);
        });
    }

    @Test
    void testUnknownDataSources() {
        assertThrows(IllegalStateException.class, () -> {
            BalancingDataSourceProperties properties = new BalancingDataSourceProperties();
            properties.setDatasources(new LinkedHashMap<>());
            properties.getDatasources().put("second", new ExtendedDataSourceProperties()); //incorrect order
            properties.getDatasources().put("first", new ExtendedDataSourceProperties());
            properties.setOrder("whatever, and again");

            properties.makeSureDataSourceOrder(IGNORE_STRATEGY);
        });
    }

    @Test
    void testIgnoredDataSources() {
        BalancingDataSourceProperties properties = new BalancingDataSourceProperties();
        properties.setDatasources(new LinkedHashMap<>());
        properties.getDatasources().put("second", new ExtendedDataSourceProperties());

        properties.getDatasources().put("first", new ExtendedDataSourceProperties());
        ExtendedDataSourceProperties ignoreDataSourceProperties = new ExtendedDataSourceProperties();
        ignoreDataSourceProperties.setUrl("IGNORE");
        properties.getDatasources().put("ignore_datasource", ignoreDataSourceProperties);

        properties.setOrder("first, second");

        properties.makeSureDataSourceOrder(IGNORE_STRATEGY);

        val iterator = properties.getDatasources().keySet().iterator();
        properties.getDatasources().entrySet();
        assertEquals("first", iterator.next());
        assertEquals("second", iterator.next());
        assertEquals("ignore_datasource", iterator.next());
    }
}
