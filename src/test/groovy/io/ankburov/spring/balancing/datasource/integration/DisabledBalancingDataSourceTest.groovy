package io.ankburov.spring.balancing.datasource.integration

import groovy.transform.CompileStatic
import io.ankburov.spring.balancing.datasource.config.TestConfiguration
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@CompileStatic
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestConfiguration.class, properties = ["spring.balancing-config.balancing.overrideBalancingDataSource=true"])
class DisabledBalancingDataSourceTest {

    @Test
    void contextLoads() {
    }
}
