package io.ankburov.spring.balancing.datasource.model;

import java.sql.SQLException;

@FunctionalInterface
public interface SqlFunction<T, R> {

    R apply(T argument) throws SQLException;
}
