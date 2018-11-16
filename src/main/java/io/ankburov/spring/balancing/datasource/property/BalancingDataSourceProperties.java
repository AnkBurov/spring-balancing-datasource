package io.ankburov.spring.balancing.datasource.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.balancing-dataSources-config")
public class BalancingDataSourceProperties {

    private Map<String, ExtendedDataSourceProperties> dataSources;

    private boolean overrideBalancingDataSource = false;
}
