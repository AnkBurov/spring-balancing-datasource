package io.ankburov.spring.balancing.datasource.failed;

import io.ankburov.spring.balancing.datasource.model.NamedFailAwareDataSource;

public class AlwaysUpdateFailedDataSourceStrategy implements UpdateFailedDataSourceStrategy {

    @Override
    public void update(NamedFailAwareDataSource failedDataSource) {
        // race condition doesn't matter here - doesn't matter which thread will update the field by his version of LocalDateTime.now()
        failedDataSource.setFailed();
    }
}
