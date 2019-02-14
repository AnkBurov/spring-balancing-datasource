package io.ankburov.spring.balancing.datasource.integration

import groovy.transform.CompileStatic
import io.ankburov.spring.balancing.datasource.BalancingDataSource
import io.ankburov.spring.balancing.datasource.config.TestConfiguration
import io.ankburov.spring.balancing.datasource.factory.HikariDataSourceFactory
import io.ankburov.spring.balancing.datasource.metadata.HikariBalancingDataSourcePublicMetrics
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@CompileStatic
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestConfiguration.class, properties = ["spring.balancing-config.balancing.overrideBalancingDataSource=true"])
class DisabledBalancingDataSourceTest {

    @Autowired
    ApplicationContext applicationContext

    @Test
    void contextLoads() {
        Assert.assertTrue(applicationContext.getBeanNamesForType(BalancingDataSource).size() == 0)
        Assert.assertTrue(applicationContext.getBeanNamesForType(HikariDataSourceFactory).size() == 1)
        Assert.assertTrue(applicationContext.getBeanNamesForType(HikariBalancingDataSourcePublicMetrics).size() == 0)
    }
}
