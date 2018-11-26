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
        properties.setDataSources(new LinkedHashMap<>());
        properties.getDataSources().put("second", new ExtendedDataSourceProperties()); //incorrect order
        properties.getDataSources().put("first", new ExtendedDataSourceProperties());
        properties.setDataSourcesOrder("first, second");

        properties.makeSureDataSourceOrder(IGNORE_STRATEGY);

        val iterator = properties.getDataSources().keySet().iterator();
        properties.getDataSources().entrySet();
        assertEquals("first", iterator.next());
        assertEquals("second", iterator.next());
    }

    @Test
    void testEmptyOrder() {
        assertThrows(IllegalStateException.class, () -> {
            BalancingDataSourceProperties properties = new BalancingDataSourceProperties();
            properties.setDataSources(new LinkedHashMap<>());
            properties.getDataSources().put("second", new ExtendedDataSourceProperties());
            properties.getDataSources().put("first", new ExtendedDataSourceProperties());

            properties.makeSureDataSourceOrder(IGNORE_STRATEGY);
        });
    }

    @Test
    void testOrderIllegalSize() {
        assertThrows(IllegalStateException.class, () -> {
            BalancingDataSourceProperties properties = new BalancingDataSourceProperties();
            properties.setDataSources(new LinkedHashMap<>());
            properties.getDataSources().put("second", new ExtendedDataSourceProperties()); //incorrect order
            properties.getDataSources().put("first", new ExtendedDataSourceProperties());
            properties.setDataSourcesOrder("first");

            properties.makeSureDataSourceOrder(IGNORE_STRATEGY);
        });
    }

    @Test
    void testUnknownDataSources() {
        assertThrows(IllegalStateException.class, () -> {
            BalancingDataSourceProperties properties = new BalancingDataSourceProperties();
            properties.setDataSources(new LinkedHashMap<>());
            properties.getDataSources().put("second", new ExtendedDataSourceProperties()); //incorrect order
            properties.getDataSources().put("first", new ExtendedDataSourceProperties());
            properties.setDataSourcesOrder("whatever, and again");

            properties.makeSureDataSourceOrder(IGNORE_STRATEGY);
        });
    }

    @Test
    void testIgnoredDataSources() {
        BalancingDataSourceProperties properties = new BalancingDataSourceProperties();
        properties.setDataSources(new LinkedHashMap<>());
        properties.getDataSources().put("second", new ExtendedDataSourceProperties());

        properties.getDataSources().put("first", new ExtendedDataSourceProperties());
        ExtendedDataSourceProperties ignoreDataSourceProperties = new ExtendedDataSourceProperties();
        ignoreDataSourceProperties.setUrl("IGNORE");
        properties.getDataSources().put("ignore_datasource", ignoreDataSourceProperties);

        properties.setDataSourcesOrder("first, second");

        properties.makeSureDataSourceOrder(IGNORE_STRATEGY);

        val iterator = properties.getDataSources().keySet().iterator();
        properties.getDataSources().entrySet();
        assertEquals("first", iterator.next());
        assertEquals("second", iterator.next());
        assertEquals("ignore_datasource", iterator.next());
    }
}
