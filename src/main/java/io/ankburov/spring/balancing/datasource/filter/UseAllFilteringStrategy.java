package io.ankburov.spring.balancing.datasource.filter;

import io.ankburov.spring.balancing.datasource.model.NamedFailAwareDataSource;

import java.util.List;

public class UseAllFilteringStrategy implements FilteringStrategy {

    @Override
    public List<NamedFailAwareDataSource> filter(List<NamedFailAwareDataSource> dataSources) {
        return dataSources;
    }
}
