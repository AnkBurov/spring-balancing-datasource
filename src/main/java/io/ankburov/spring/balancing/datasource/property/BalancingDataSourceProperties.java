package io.ankburov.spring.balancing.datasource.property;

import io.ankburov.spring.balancing.datasource.ignore.IgnoreDataSourceStrategy;
import io.ankburov.spring.balancing.datasource.model.BalancingType;
import io.ankburov.spring.balancing.datasource.model.FilteringType;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "spring.balancing-dataSources-config")
public class BalancingDataSourceProperties {

    /**
     * Map of balancing datasources
     */
    private Map<String, ExtendedDataSourceProperties> dataSources;

    /**
     * Order of dataSources names (map keys) splitted by a separator
     */
    private String dataSourcesOrder;

    /**
     * DataSourcesOrder string separator
     */
    private String dataSourcesOrderSeparator = ",";

    /**
     * Try to get a connection from the built balancing datasource after initialization
     */
    private boolean validateDataSourceAtStart = true;

    @NestedConfigurationProperty
    private Balancing balancing = new Balancing();

    @NestedConfigurationProperty
    private Logging logging = new Logging();

    @NestedConfigurationProperty
    private Filtering filtering = new Filtering();

    @Getter
    @Setter
    public static class Balancing {

        /**
         * Balancing type - sequential FAILOVER or load-balancing RANDOM distribution
         */
        private BalancingType type = BalancingType.FAILOVER;

        /**
         * Do not create the balancing datasource bean if true
         */
        private boolean overrideBalancingDataSource = false;
    }

    @Getter
    @Setter
    public static class Logging {

        /**
         * Log to WARN failed datasource if previously failed more than n minutes ago (or din't fail at all)
         */
        private Duration timeThreshold = Duration.ofMinutes(1);
    }

    @Getter
    @Setter
    public static class Filtering {

        /**
         * Filtering type - how to filter out previously failed datasources
         */
        private FilteringType type = FilteringType.ALL;

        /**
         * Use only working datasources or those who failed old enough
         */
        private Duration timeThreshold = Duration.ofMinutes(1);
    }

    /**
     * Make sure that the datasource order is desirable
     */
    public void makeSureDataSourceOrder(IgnoreDataSourceStrategy ignoreDataSourceStrategy) {
        if (StringUtils.isEmpty(dataSourcesOrder)) {
            throw new IllegalStateException("Property dataSourceOrder cannot be empty");
        }
        val notIgnoredDataSources = ignoreDataSourceStrategy.filterNotIgnored(dataSources);

        String[] splittedDataSourcesOrder = dataSourcesOrder.split(dataSourcesOrderSeparator);
        if (splittedDataSourcesOrder.length != notIgnoredDataSources.size()) {
            throw new IllegalStateException("Splitted size of property dataSourceOrder not equals to dataSources");
        }

        Map<String, ExtendedDataSourceProperties> orderedDataSources = new LinkedHashMap<>();
        String dataSourceNotFound = "DataSource %s is not found among the datasource";
        Arrays.stream(splittedDataSourcesOrder).sequential()
                .map(String::trim)
                .forEachOrdered((String dataSourceName) -> {
                    if (!dataSources.containsKey(dataSourceName)) {
                        throw new IllegalStateException(String.format(dataSourceNotFound, dataSourceName));
                    }
                    orderedDataSources.put(dataSourceName, dataSources.get(dataSourceName));
                });

        // add ignored datasources
        orderedDataSources.putAll(ignoreDataSourceStrategy.filterIgnored(dataSources));

        if (orderedDataSources.size() != dataSources.size()) {
            throw new IllegalStateException("Ordered dataSources size not equals not initial dataSources size");
        }
        dataSources = orderedDataSources;
    }
}
