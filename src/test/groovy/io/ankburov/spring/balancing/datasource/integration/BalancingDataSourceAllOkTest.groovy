package io.ankburov.spring.balancing.datasource.integration

import groovy.sql.Sql
import groovy.transform.CompileStatic
import io.ankburov.spring.balancing.datasource.BalancingDataSource
import io.ankburov.spring.balancing.datasource.config.TestConfiguration
import io.ankburov.spring.balancing.datasource.integration.delegate.MariaDelegate
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.JdbcDatabaseContainer

import static org.junit.jupiter.api.Assertions.assertEquals

@CompileStatic
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestConfiguration.class)
class BalancingDataSourceAllOkTest {

    private static final JdbcDatabaseContainer FIRST_MARIA = new MariaDelegate("first").init()

    @Autowired
    private BalancingDataSource balancingDataSource;

    @Test
    void testAllDataSourcesAvailable() {
        for (int i = 0; i < 100; i++) {
            new Sql(balancingDataSource.getConnection()).withCloseable { sql ->
                def resultSet = sql.firstRow("select 1 as res")
                assertEquals(1, resultSet.size())
                assertEquals(1, resultSet["res"])
            }
        }
    }
}
