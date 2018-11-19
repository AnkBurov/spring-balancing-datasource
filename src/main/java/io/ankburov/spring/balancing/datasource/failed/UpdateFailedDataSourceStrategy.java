package io.ankburov.spring.balancing.datasource.failed;

import io.ankburov.spring.balancing.datasource.model.NamedFailAwareDataSource;

public interface UpdateFailedDataSourceStrategy {

    void update(NamedFailAwareDataSource failedDataSource);
}
