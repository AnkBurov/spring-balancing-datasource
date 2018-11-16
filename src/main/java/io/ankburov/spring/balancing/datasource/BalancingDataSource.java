package io.ankburov.spring.balancing.datasource;

import io.ankburov.spring.balancing.datasource.property.BalancingDataSourceProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.AbstractDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class BalancingDataSource extends AbstractDataSource implements InitializingBean {

    private final BalancingDataSourceProperties properties;

    private List<DataSource> dataSources;

    @Override
    public void afterPropertiesSet() {
//        properties.getDataSources().entrySet().stream()
//                  .peek(entry -> log.info("Creating datasource with id {}", entry.getKey()))
//                  .map(Map.Entry::getValue)
//                  .map(dataSourceProps -> dataSourceProps.getAdditionalProperties())




        /*List<DataSource> dataSources = serviceProperties.getDataSources().entrySet().stream()
                                                .peek(entry -> log.info("Creating datasource with id {}", entry.getKey()))
                                                .map(Map.Entry::getValue)
                                                .map(DataSourceProperties::initializeDataSourceBuilder)
                                                .map(DataSourceBuilder::build)
                                                .collect(Collectors.toList());*/
    }

    @Override
    public Connection getConnection() throws SQLException {
        return null;
    }

    @Override
    public Connection getConnection(String s, String s1) throws SQLException {
        return null;
    }
}
