package io.ankburov.spring.balancing.datasource.integration

import groovy.sql.Sql
import groovy.transform.CompileStatic
import io.ankburov.spring.balancing.datasource.BalancingDataSource
import io.ankburov.spring.balancing.datasource.config.TestConfiguration
import io.ankburov.spring.balancing.datasource.exception.NoAvailableDataSourcesException
import io.ankburov.spring.balancing.datasource.integration.delegate.MariaDelegate
import io.ankburov.spring.balancing.datasource.property.BalancingDataSourceProperties
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.JdbcDatabaseContainer

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertThrows
import static org.junit.jupiter.api.Assertions.assertTrue

@CompileStatic
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestConfiguration.class)
class BalancingDataSourceTest {

    private static final JdbcDatabaseContainer FIRST_MARIA = new MariaDelegate("first").init()
    private static final JdbcDatabaseContainer SECOND_MARIA = new MariaDelegate("second").init()

    @Autowired
    private BalancingDataSource balancingDataSource;

    @Autowired
    private BalancingDataSourceProperties balancingDataSourceProperties

    @BeforeAll
    static void beforeClass() {
        System.setProperty("spring.balancing-dataSources-config.dataSources.ignore_data_source.url", "IGNORE")
    }

    @Test
    void testAllDataSourcesAvailable() {
        startIfNotStarted(FIRST_MARIA, SECOND_MARIA)
        testDataSource()
    }

    @Test
    void testDataSourcesSize() {
        startIfNotStarted(FIRST_MARIA, SECOND_MARIA)
        assertEquals(2, balancingDataSource.getDataSources().size())
    }

    @Test
    void testOneFailedDataSourceAndConnectionIsAvailable() {
        stopIfNotStopped(FIRST_MARIA) // explicitly break one of datasources
        startIfNotStarted(SECOND_MARIA)
        testDataSource()
    }

    @Test
    void testAllDataSourcesFailed() {
        stopIfNotStopped(FIRST_MARIA, SECOND_MARIA)
        assertThrows(NoAvailableDataSourcesException.class, new Executable() {
            @Override
            void execute() throws Throwable {
                testDataSource()
            }
        })
    }

    @Test
    void testThatOrderOfDataSourcesWillBePreserved() {
        assertTrue(balancingDataSourceProperties.getDataSources() instanceof LinkedHashMap)
    }

    private void testDataSource() {
        for (int i = 0; i < 100; i++) {
            new Sql(balancingDataSource.getConnection()).withCloseable { sql ->
                def resultSet = sql.firstRow("select 1 as res")
                assertEquals(1, resultSet.size())
                assertEquals(1, resultSet["res"])
            }
        }
    }

    private static void startIfNotStarted(JdbcDatabaseContainer... containers) {
        containers.each { container ->
            if (!container.isRunning()) {
                container.start()
            }
        }
    }

    private static void stopIfNotStopped(JdbcDatabaseContainer... containers) {
        containers.each { container ->
            if (container.isRunning()) {
                container.stop()
            }
        }
    }
}
