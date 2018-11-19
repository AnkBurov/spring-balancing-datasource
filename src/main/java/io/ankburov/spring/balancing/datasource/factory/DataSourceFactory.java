package io.ankburov.spring.balancing.datasource.factory;

import io.ankburov.spring.balancing.datasource.property.ExtendedDataSourceProperties;

import javax.sql.DataSource;

public interface DataSourceFactory {

    DataSource create(ExtendedDataSourceProperties dataSourceProperties);
}
