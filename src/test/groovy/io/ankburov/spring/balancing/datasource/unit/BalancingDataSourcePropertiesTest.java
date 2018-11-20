package io.ankburov.spring.balancing.datasource.unit;

import io.ankburov.spring.balancing.datasource.property.BalancingDataSourceProperties;
import io.ankburov.spring.balancing.datasource.property.ExtendedDataSourceProperties;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BalancingDataSourcePropertiesTest {

    @Test
    void testOrderCorrect() {
        BalancingDataSourceProperties properties = new BalancingDataSourceProperties();
        properties.setDataSources(new LinkedHashMap<>());
        properties.getDataSources().put("second", new ExtendedDataSourceProperties()); //incorrect order
        properties.getDataSources().put("first", new ExtendedDataSourceProperties());
        properties.setDataSourcesOrder("first, second");

        properties.makeSureDataSourceOrder();

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

            properties.makeSureDataSourceOrder();
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

            properties.makeSureDataSourceOrder();
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

            properties.makeSureDataSourceOrder();
        });
    }
}
