package io.ankburov.spring.balancing.datasource.balancingtype;

import io.ankburov.spring.balancing.datasource.model.NamedFailAwareDataSource;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBalancingStrategy implements BalancingStrategy {

    @Override
    public List<NamedFailAwareDataSource> apply(List<NamedFailAwareDataSource> dataSources) {
        List<NamedFailAwareDataSource> clonedDataSources = new ArrayList<>(dataSources);
        return applyInternal(clonedDataSources);
    }

    protected abstract List<NamedFailAwareDataSource> applyInternal(List<NamedFailAwareDataSource> dataSources);
}
