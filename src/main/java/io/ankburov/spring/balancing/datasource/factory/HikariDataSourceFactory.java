package io.ankburov.spring.balancing.datasource.factory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.ankburov.spring.balancing.datasource.property.ExtendedDataSourceProperties;

import javax.sql.DataSource;
import java.util.Properties;

public class HikariDataSourceFactory implements DataSourceFactory {

    @Override
    public DataSource create(ExtendedDataSourceProperties dataSourceProperties) {
        Properties properties = new Properties();
        properties.put("jdbcUrl", dataSourceProperties.determineUrl());
        properties.put("username", dataSourceProperties.determineUsername());
        properties.put("password", dataSourceProperties.determinePassword());
        properties.put("poolName", dataSourceProperties.getName());

        // to avoid default long connection timeout of Hikari
        if (!dataSourceProperties.getAdditionalProperties().containsKey("connectionTimeout")) {
            properties.put("connectionTimeout", 1000);
        }

        // disable datasource check on the start, if not said otherwise
        if (!dataSourceProperties.getAdditionalProperties().containsKey("initializationFailTimeout")) {
            properties.put("initializationFailTimeout", 0);
        }

        dataSourceProperties.getAdditionalProperties().forEach(properties::put);

        HikariConfig hikariConfig = new HikariConfig(properties);
        return new HikariDataSource(hikariConfig);
    }
}
