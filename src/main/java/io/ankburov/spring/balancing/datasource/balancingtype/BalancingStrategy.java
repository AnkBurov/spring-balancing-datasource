package io.ankburov.spring.balancing.datasource.balancingtype;

import io.ankburov.spring.balancing.datasource.model.NamedFailAwareDataSource;

import java.util.List;

public interface BalancingStrategy {

    List<NamedFailAwareDataSource> apply(List<NamedFailAwareDataSource> dataSources);
}
