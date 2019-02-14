package io.ankburov.spring.balancing.datasource.integration

import com.github.dockerjava.api.DockerClient
import groovy.transform.CompileStatic
import io.ankburov.spring.balancing.datasource.config.TestConfiguration
import io.ankburov.spring.balancing.datasource.integration.delegate.MariaDelegate
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.DockerClientFactory
import org.testcontainers.containers.JdbcDatabaseContainer

/**
 * @author ianazarov
 * (c) RGS
 * created 2019-02-14
 */
@CompileStatic
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = ["management.security.enabled=false", "spring.balancing-config.balancing.enableMetricsForDB=hikari"])
@EnableAutoConfiguration
class HikariPublicMetricsTest {
    private static final DockerClient DOCKER = DockerClientFactory.instance().client();

    private static final JdbcDatabaseContainer FIRST_MARIA = new MariaDelegate("first").init()
    private static final JdbcDatabaseContainer SECOND_MARIA = new MariaDelegate("second").init()
    @Autowired
    private TestRestTemplate testRestTemplate

    @Test
    void testAllDataSourcesAvailable() {
        def entity = (HashMap<String, String>) testRestTemplate.getForEntity("/metrics", HashMap).getBody()
        Assertions.assertNotNull(entity.get("datasource.first.active"))
        Assertions.assertNotNull(entity.get("datasource.first.usage"))
        Assertions.assertNotNull(entity.get("datasource.second.active"))
        Assertions.assertNotNull(entity.get("datasource.second.usage"))
    }
}
