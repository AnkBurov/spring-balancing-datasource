package io.ankburov.spring.balancing.datasource.log;

import io.ankburov.spring.balancing.datasource.model.NamedFailAwareDataSource;

public interface FailedDataSourceLogStrategy {

    void log(NamedFailAwareDataSource failedDataSource, Exception e);
}
