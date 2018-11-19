package io.ankburov.spring.balancing.datasource;

import io.ankburov.spring.balancing.datasource.balancingtype.BalancingStrategy;
import io.ankburov.spring.balancing.datasource.exception.NoAvailableDataSourcesException;
import io.ankburov.spring.balancing.datasource.factory.DataSourceFactory;
import io.ankburov.spring.balancing.datasource.failed.UpdateFailedDataSourceStrategy;
import io.ankburov.spring.balancing.datasource.filter.FilteringStrategy;
import io.ankburov.spring.balancing.datasource.log.FailedDataSourceLogStrategy;
import io.ankburov.spring.balancing.datasource.model.NamedFailAwareDataSource;
import io.ankburov.spring.balancing.datasource.model.SqlFunction;
import io.ankburov.spring.balancing.datasource.property.BalancingDataSourceProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

/**
 * todo linkedmap properties test
 */
@Slf4j
@RequiredArgsConstructor
public class BalancingDataSource extends AbstractDataSource implements InitializingBean {

    private final DataSourceFactory dataSourceFactory;

    private final BalancingDataSourceProperties properties;

    private final FilteringStrategy filteringStrategy;

    private final BalancingStrategy balancingStrategy;

    private final FailedDataSourceLogStrategy failedDataSourceLogStrategy;

    private final UpdateFailedDataSourceStrategy updateFailedDataSourceStrategy;

    private List<NamedFailAwareDataSource> dataSources;

    @Override
    public void afterPropertiesSet() {
        if (CollectionUtils.isEmpty(properties.getDataSources())) {
            throw new IllegalStateException("Balancing datasources list cannot be empty");
        }
        dataSources = properties.getDataSources().entrySet().stream()
                                .peek(entry -> log.info("Creating datasource with id {}", entry.getKey()))
                                .map(entry -> {
                                    val dataSourceName = entry.getKey();
                                    val dataSourceProps = entry.getValue();
                                    DataSource dataSource = dataSourceFactory.create(dataSourceProps);

                                    return new NamedFailAwareDataSource(dataSourceName, dataSource);
                                }).collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getBalancedConnection(DataSource::getConnection);
    }

    @Override
    public Connection getConnection(String s, String s1) throws SQLException {
        return getBalancedConnection(datasource -> datasource.getConnection(s, s1));
    }

    private Connection getBalancedConnection(SqlFunction<DataSource, Connection> connectionProducer) throws SQLException { //todo add check on start
        val filteredDataSources = filteringStrategy.filter(dataSources);
        val balancingDataSources = balancingStrategy.apply(filteredDataSources);

        for (NamedFailAwareDataSource dataSource : balancingDataSources) {
            try {
                Connection connection = connectionProducer.apply(dataSource);
                dataSource.setNotFailed();
                return connection;
            } catch (Exception e) {
                failedDataSourceLogStrategy.log(dataSource, e);
                updateFailedDataSourceStrategy.update(dataSource);
            }
        }

        throw new NoAvailableDataSourcesException();
    }
}
