package io.ankburov.spring.balancing.datasource.metadata;

import com.zaxxer.hikari.HikariDataSource;
import io.ankburov.spring.balancing.datasource.BalancingDataSource;
import io.ankburov.spring.balancing.datasource.model.NamedFailAwareDataSource;
import javafx.util.Pair;
import lombok.Getter;
import org.springframework.boot.actuate.endpoint.DataSourcePublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.autoconfigure.jdbc.metadata.HikariDataSourcePoolMetadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class provides balancing-datasource specific implementation of Hikari Pool usage statistics.
 * This statistics available at actuator' /metrics endpoint and provides 2 values - count and usage of DB Pool.
 * Each of balanced DB has key prefix 'datasource.%dbName here%.', for ex. 'datasource.maindb.usage: 0.0'
 * @author arxmim
 * created 2019-01-28
 */
public class BalancingDataSourcePublicMetrics extends DataSourcePublicMetrics {

    @Getter
    private DataSourcePublicMetrics dataSourcePublicMetrics = new DataSourcePublicMetrics();

    private final BalancingDataSource balancingDataSource;

    public BalancingDataSourcePublicMetrics(BalancingDataSource balancingDataSource) {
        this.balancingDataSource = balancingDataSource;
    }

    /**
     * Actual list of metrics for DB. It invokes basic implementation (if any) and add balancing-datasource specific implementation, based on fact that
     * spring-initialized javax.sql.Datasource (io.ankburov.spring.balancing.datasource.BalancingDataSource) actually contains list of manually created Hikari datasources.
     * @return
     */
    @Override
    public Collection<Metric<?>> metrics() {
        Collection<Metric<?>> result = dataSourcePublicMetrics.metrics();
        result.addAll(balancingDataSource.getDataSources()
                                         .stream()
                                         .map(this::splitToNameAndPoolMetadata)
                                         .map(this::getMetricsFromDataSourcePoolMetadata)
                                         .flatMap(Collection::stream)
                                         .collect(Collectors.toList()));
        return result;
    }

    private Pair<String, HikariDataSourcePoolMetadata> splitToNameAndPoolMetadata(NamedFailAwareDataSource namedFailAwareDataSource) {
        HikariDataSourcePoolMetadata poolMetadata = Optional.of(namedFailAwareDataSource)
                                                            .map(NamedFailAwareDataSource::getDataSource)
                                                            .map(HikariDataSource.class::cast)
                                                            .map(HikariDataSourcePoolMetadata::new)
                                                            .orElseThrow(IllegalStateException::new);
        return new Pair<>(namedFailAwareDataSource.getName(), poolMetadata);
    }

    private Collection<Metric<?>> getMetricsFromDataSourcePoolMetadata(Pair<String, HikariDataSourcePoolMetadata> hikariDataSourcePair) {
        ArrayList<Metric<?>> list = new ArrayList<>();
        String prefix = "datasource." + hikariDataSourcePair.getKey() + ".";
        list.add(new Metric<>(prefix + "active", hikariDataSourcePair.getValue().getActive()));
        list.add(new Metric<>(prefix + "usage", hikariDataSourcePair.getValue().getUsage()));
        return list;
    }
}
