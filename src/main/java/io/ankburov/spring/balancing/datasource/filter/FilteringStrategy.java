package io.ankburov.spring.balancing.datasource.filter;

import io.ankburov.spring.balancing.datasource.model.NamedFailAwareDataSource;

import java.util.List;

public interface FilteringStrategy {

    List<NamedFailAwareDataSource> filter(List<NamedFailAwareDataSource> dataSources);
}
