package io.ankburov.spring.balancing.datasource.filter;

import io.ankburov.spring.balancing.datasource.model.NamedFailAwareDataSource;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Use only working datasources or those who failed old enough
 */
@RequiredArgsConstructor
public class OnlyWorkingFilteringStrategy implements FilteringStrategy {

    private final Duration timeThreshold;

    @Override
    public List<NamedFailAwareDataSource> filter(List<NamedFailAwareDataSource> dataSources) {
        val filteredDataSources = dataSources.stream()
                                             .filter(dataSource -> dataSource.hasFailedOldEnoughOrOk(timeThreshold))
                                             .collect(Collectors.toList());
        if (filteredDataSources.isEmpty()) { // if all datasources has failed, then return initial datasources and pray for the best
            return dataSources;
        }
        return filteredDataSources;
    }
}
