package io.ankburov.spring.balancing.datasource.factory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.ankburov.spring.balancing.datasource.property.ExtendedDataSourceProperties;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
public class HikariDataSourceFactory implements DataSourceFactory {

    private static final String IGNORE = "IGNORE";

    @Override
    public DataSource create(ExtendedDataSourceProperties dataSourceProperties) {
        Properties properties = new Properties();
        if (dataSourceProperties.getUrl() != null) {
            properties.put("jdbcUrl", dataSourceProperties.determineUrl()); // NPE-dangerous method
        }
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

        // add additional properties if their values are not ignored
        dataSourceProperties.getAdditionalProperties().forEach((key, value) -> {
            if (!IGNORE.equalsIgnoreCase(value)) {
                properties.put(key, value);
            }
        });
        log.info("Datasource properties are {}", properties.toString());

        HikariConfig hikariConfig = new HikariConfig(properties);
        return new HikariDataSource(hikariConfig);
    }
}
