package io.ankburov.spring.balancing.datasource.log;

import io.ankburov.spring.balancing.datasource.model.NamedFailAwareDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

/**
 * If datasource previously failed more than n time ago (or din't fail at all) then log to WARN
 * Log to DEBUG otherwise
 */
@Slf4j
@RequiredArgsConstructor
public class TimedFailedDataSourceLogStrategy implements FailedDataSourceLogStrategy {

    private static final String LOG_MESSAGE = "Failed to get a connection from the datasource {}";

    private final Duration logToWarnDuration;

    @Override
    public void log(NamedFailAwareDataSource failedDataSource, Exception e) {
        if (failedDataSource.hasFailedOldEnoughOrOk(logToWarnDuration)) {
            log.warn(LOG_MESSAGE, e);
        } else {
            log.debug(LOG_MESSAGE, e);
        }
    }
}
