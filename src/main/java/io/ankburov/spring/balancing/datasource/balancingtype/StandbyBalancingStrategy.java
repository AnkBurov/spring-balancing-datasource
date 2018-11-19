package io.ankburov.spring.balancing.datasource.balancingtype;

import io.ankburov.spring.balancing.datasource.model.NamedFailAwareDataSource;

import java.util.List;

/**
 * Return list of datasources as is without shuffling order
 * <p>
 * Strategy preserves the order of datasources
 */
public class StandbyBalancingStrategy extends AbstractBalancingStrategy {

    @Override
    protected List<NamedFailAwareDataSource> applyInternal(List<NamedFailAwareDataSource> dataSources) {
        return dataSources;
    }
}
