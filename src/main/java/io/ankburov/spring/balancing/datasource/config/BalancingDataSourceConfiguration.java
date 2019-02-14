package io.ankburov.spring.balancing.datasource.config;

import io.ankburov.spring.balancing.datasource.BalancingDataSource;
import io.ankburov.spring.balancing.datasource.balancingtype.BalancingStrategy;
import io.ankburov.spring.balancing.datasource.balancingtype.FailOverBalancingStrategy;
import io.ankburov.spring.balancing.datasource.balancingtype.MostHealthyFirstBalancingStrategy;
import io.ankburov.spring.balancing.datasource.balancingtype.RandomBalancingStrategy;
import io.ankburov.spring.balancing.datasource.factory.DataSourceFactory;
import io.ankburov.spring.balancing.datasource.factory.HikariDataSourceFactory;
import io.ankburov.spring.balancing.datasource.failed.AlwaysUpdateFailedDataSourceStrategy;
import io.ankburov.spring.balancing.datasource.failed.UpdateFailedDataSourceStrategy;
import io.ankburov.spring.balancing.datasource.filter.FilteringStrategy;
import io.ankburov.spring.balancing.datasource.filter.OnlyWorkingFilteringStrategy;
import io.ankburov.spring.balancing.datasource.filter.UseAllFilteringStrategy;
import io.ankburov.spring.balancing.datasource.ignore.IgnoreDataSourceByUrlStrategy;
import io.ankburov.spring.balancing.datasource.ignore.IgnoreDataSourceStrategy;
import io.ankburov.spring.balancing.datasource.log.FailedDataSourceLogStrategy;
import io.ankburov.spring.balancing.datasource.log.TimedFailedDataSourceLogStrategy;
import io.ankburov.spring.balancing.datasource.metadata.HikariBalancingDataSourcePublicMetrics;
import io.ankburov.spring.balancing.datasource.property.BalancingDataSourceProperties;
import org.springframework.boot.actuate.endpoint.DataSourcePublicMetrics;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(BalancingDataSourceProperties.class)
public class BalancingDataSourceConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public IgnoreDataSourceStrategy ignoreDataSourceByUrlStrategy() {
        return new IgnoreDataSourceByUrlStrategy();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.balancing-config.balancing", value = "type", havingValue = "FAILOVER")
    public BalancingStrategy failoverBalancingStrategy() {
        return new FailOverBalancingStrategy();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.balancing-config.balancing", value = "type", havingValue = "RANDOM")
    public BalancingStrategy randomBalancingStrategy() {
        return new RandomBalancingStrategy();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.balancing-config.balancing", value = "type", havingValue = "MOST_HEALTHY", matchIfMissing = true)
    public BalancingStrategy mostHealthyFirstBalancingStrategy() {
        return new MostHealthyFirstBalancingStrategy();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.balancing-config.filtering", value = "type", havingValue = "ALL", matchIfMissing = true)
    public FilteringStrategy useAllFilteringStrategy() {
        return new UseAllFilteringStrategy();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.balancing-config.filtering", value = "type", havingValue = "ONLY_WORKING")
    public FilteringStrategy onlyWorkingFilteringStrategy(BalancingDataSourceProperties properties) {
        return new OnlyWorkingFilteringStrategy(properties.getFiltering().getTimeThreshold());
    }

    @Bean
    @ConditionalOnMissingBean
    public FailedDataSourceLogStrategy timedFailedDataSourceLogStrategy(BalancingDataSourceProperties properties) {
        return new TimedFailedDataSourceLogStrategy(properties.getLogging().getTimeThreshold());
    }

    @Bean
    @ConditionalOnMissingBean
    public UpdateFailedDataSourceStrategy alwaysUpdateFailedDataSourceStrategy() {
        return new AlwaysUpdateFailedDataSourceStrategy();
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSourceFactory hikariDataSourceFactory() {
        return new HikariDataSourceFactory();
    }

    @Bean("dataSource")
    @ConditionalOnProperty(prefix = "spring.balancing-config.balancing", value = "overrideBalancingDataSource", havingValue = "false", matchIfMissing = true)
    public DataSource dataSource(IgnoreDataSourceStrategy ignoreDataSourceStrategy,
                                 BalancingDataSourceProperties properties,
                                 DataSourceFactory dataSourceFactory,
                                 FilteringStrategy filteringStrategy,
                                 BalancingStrategy balancingStrategy,
                                 FailedDataSourceLogStrategy failedDataSourceLogStrategy,
                                 UpdateFailedDataSourceStrategy updateFailedDataSourceStrategy) {
        return new BalancingDataSource(ignoreDataSourceStrategy, dataSourceFactory, properties, filteringStrategy, balancingStrategy,
                                       failedDataSourceLogStrategy, updateFailedDataSourceStrategy);
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.balancing-config.balancing", value = "enableMetricsForDB", havingValue = "hikari")
    public DataSourcePublicMetrics hikariBalancingDataSourcePublicMetrics(BalancingDataSource balancingDataSource) {
        return new HikariBalancingDataSourcePublicMetrics(balancingDataSource);
    }
}
