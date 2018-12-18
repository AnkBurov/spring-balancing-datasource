package io.ankburov.spring.balancing.datasource.unit;

import com.zaxxer.hikari.HikariDataSource;
import io.ankburov.spring.balancing.datasource.factory.HikariDataSourceFactory;
import io.ankburov.spring.balancing.datasource.property.ExtendedDataSourceProperties;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HikariDataSourceFactoryTest {

    @Test
    void testOk() {
        ExtendedDataSourceProperties dataSourceProperties = new ExtendedDataSourceProperties();
        dataSourceProperties.setUrl("jdbc:mariadb://localhost:3306");
        dataSourceProperties.setUsername("some");
        dataSourceProperties.setPassword("some");

        dataSourceProperties.getAdditionalProperties().put("schema", "someSchema");

        HikariDataSourceFactory dataSourceFactory = new HikariDataSourceFactory();
        HikariDataSource dataSource = (HikariDataSource) dataSourceFactory.create(dataSourceProperties);

        assertEquals(dataSourceProperties.getUrl(), dataSource.getJdbcUrl());
        assertEquals(dataSourceProperties.getAdditionalProperties().get("schema"), dataSource.getSchema());
    }

    @Test
    void testIgnoringAdditionalProperties() {
        ExtendedDataSourceProperties dataSourceProperties = new ExtendedDataSourceProperties();
        dataSourceProperties.setUrl("jdbc:mariadb://localhost:3306");
        dataSourceProperties.setUsername("some");
        dataSourceProperties.setPassword("some");

        dataSourceProperties.getAdditionalProperties().put("schema", "IGNORE");

        HikariDataSourceFactory dataSourceFactory = new HikariDataSourceFactory();
        HikariDataSource dataSource = (HikariDataSource) dataSourceFactory.create(dataSourceProperties);

        assertEquals(dataSourceProperties.getUrl(), dataSource.getJdbcUrl());
        assertNull(dataSource.getSchema());
    }

    @Test
    void testNullValueInAdditionalProperties() {
        ExtendedDataSourceProperties dataSourceProperties = new ExtendedDataSourceProperties();
        dataSourceProperties.setUrl("jdbc:mariadb://localhost:3306");
        dataSourceProperties.setUsername("some");
        dataSourceProperties.setPassword("some");

        dataSourceProperties.getAdditionalProperties().put("schema", null);

        HikariDataSourceFactory dataSourceFactory = new HikariDataSourceFactory();
        HikariDataSource dataSource = (HikariDataSource) dataSourceFactory.create(dataSourceProperties);

        assertEquals(dataSourceProperties.getUrl(), dataSource.getJdbcUrl());
        assertNull(dataSource.getSchema());
    }
}
