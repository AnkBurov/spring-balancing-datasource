package io.ankburov.spring.balancing.datasource.ignore;

import io.ankburov.spring.balancing.datasource.property.ExtendedDataSourceProperties;

import java.util.Map;
import java.util.stream.Collectors;

public interface IgnoreDataSourceStrategy {

    String IGNORE = "IGNORE";

    boolean ignore(ExtendedDataSourceProperties dataSourceProperties);

    default Map<String, ExtendedDataSourceProperties> filterIgnored(Map<String, ExtendedDataSourceProperties> dataSourcePropertiesMap) {
        return dataSourcePropertiesMap.entrySet().stream()
                                      .filter(entry -> ignore(entry.getValue()))
                                      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    default Map<String, ExtendedDataSourceProperties> filterNotIgnored(Map<String, ExtendedDataSourceProperties> dataSourcePropertiesMap) {
        return dataSourcePropertiesMap.entrySet().stream()
                                      .filter(entry -> !ignore(entry.getValue()))
                                      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
