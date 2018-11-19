package io.ankburov.spring.balancing.datasource.factory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.ankburov.spring.balancing.datasource.property.ExtendedDataSourceProperties;

import javax.sql.DataSource;

public class HikariDataSourceFactory implements DataSourceFactory {

    @Override
    public DataSource create(ExtendedDataSourceProperties dataSourceProperties) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dataSourceProperties.determineUrl());
        hikariConfig.setUsername(dataSourceProperties.determineUsername());
        hikariConfig.setPassword(dataSourceProperties.determinePassword());
        hikariConfig.setPoolName(dataSourceProperties.getName());
        hikariConfig.setInitializationFailTimeout(0); // disable datasource check on the start
        dataSourceProperties.getAdditionalProperties().forEach(hikariConfig::addDataSourceProperty);

        return new HikariDataSource(hikariConfig);
    }
}
