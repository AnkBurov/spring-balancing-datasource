package io.ankburov.spring.balancing.datasource.property;

import io.ankburov.spring.balancing.datasource.model.BalancingType;
import io.ankburov.spring.balancing.datasource.model.FilteringType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.Map;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "spring.balancing-dataSources-config")
public class BalancingDataSourceProperties {

    private Map<String, ExtendedDataSourceProperties> dataSources;

    @NestedConfigurationProperty
    private Balancing balancing = new Balancing();

    @NestedConfigurationProperty
    private Logging logging = new Logging();

    @NestedConfigurationProperty
    private Filtering filtering = new Filtering();

    @Getter
    @Setter
    public static class Balancing {

        private BalancingType type = BalancingType.STANDBY;

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

        private FilteringType type = FilteringType.ALL;

        /**
         * Use only working datasources or those who failed old enough
         */
        private Duration timeThreshold = Duration.ofMinutes(1);
    }
}
