package io.ankburov.spring.balancing.datasource;

import io.ankburov.spring.balancing.datasource.property.BalancingDataSourceProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class BalancingDataSourceConfiguration {

    @Configuration
    @EnableConfigurationProperties(BalancingDataSourceProperties.class)
    @ConditionalOnProperty(prefix = "spring.balancing-dataSources-config", value = "overrideBalancingDataSource", havingValue = "false")
    public class DataSourceConfiguration {

        @Bean("dataSource")
        public DataSource dataSource(BalancingDataSourceProperties properties) {
            return new BalancingDataSource(properties);
        }
    }
}
