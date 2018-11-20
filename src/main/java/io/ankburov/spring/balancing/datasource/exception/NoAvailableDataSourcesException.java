package io.ankburov.spring.balancing.datasource.exception;

import java.sql.SQLException;

public class NoAvailableDataSourcesException extends SQLException {

    private static final String ERROR_MESSAGE = "No available datasources found - all datasources returned an error instead of a connection";

    public NoAvailableDataSourcesException() {
        super(ERROR_MESSAGE);
    }
}
