package io.ankburov.spring.balancing.datasource.balancingtype;

import io.ankburov.spring.balancing.datasource.model.NamedFailAwareDataSource;

import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * Returns list of datasources by in the order of the most healthiest ones
 * <p>
 * Order:
 * - Not failed datasources at the beginning
 * - Failed datasources sorted by lastFailed asc - the oldest failed first
 */
public class MostHealthyFirstBalancingStrategy extends AbstractBalancingStrategy {
    @Override
    protected List<NamedFailAwareDataSource> applyInternal(List<NamedFailAwareDataSource> dataSources) {
        dataSources.sort(Comparator.comparing(NamedFailAwareDataSource::getLastFailed, nullsFirst(naturalOrder())));
        return dataSources;
    }
}
