package io.ankburov.spring.balancing.datasource.integration

import groovy.sql.Sql
import groovy.transform.CompileStatic
import io.ankburov.spring.balancing.datasource.BalancingDataSource
import io.ankburov.spring.balancing.datasource.config.TestConfiguration
import io.ankburov.spring.balancing.datasource.integration.delegate.MariaDelegate
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.testcontainers.containers.JdbcDatabaseContainer

import java.sql.Connection

@CompileStatic
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
class BalancingDataSourceAllOkTest {

    private static final JdbcDatabaseContainer FIRST_MARIA = new MariaDelegate("first").init()

    @Autowired
    private BalancingDataSource balancingDataSource;

    @Test
    void testAllDataSourcesAvailable() {
        for (int i = 0; i < 100; i++) {
            Connection connection = balancingDataSource.getConnection()
            def result = new Sql(connection).execute("select 1")
            Assert.assertTrue(result)
            connection.close()
        }
        println ""
    }
}
