package io.ankburov.spring.balancing.datasource.balancingtype;

import io.ankburov.spring.balancing.datasource.model.NamedFailAwareDataSource;

import java.util.Collections;
import java.util.List;

/**
 * Randomly shuffle the order of datasources to achieve random-based load balancing
 */
public class RandomBalancingStrategy extends AbstractBalancingStrategy {

    @Override
    protected List<NamedFailAwareDataSource> applyInternal(List<NamedFailAwareDataSource> dataSources) {
        Collections.shuffle(dataSources);
        return dataSources;
    }
}
