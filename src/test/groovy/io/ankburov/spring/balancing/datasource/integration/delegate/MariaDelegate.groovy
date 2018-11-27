package io.ankburov.spring.balancing.datasource.integration.delegate

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.testcontainers.containers.JdbcDatabaseContainer
import org.testcontainers.containers.MariaDBContainer

@Slf4j
@CompileStatic
class MariaDelegate {

    private final String name

    JdbcDatabaseContainer maria = (MariaDBContainer) new MariaDBContainer()

    MariaDelegate(String name) {
        this.name = name
    }

    JdbcDatabaseContainer init() {
        maria.start()

        System.setProperty("spring.balancing-config.datasources.${name}.username", maria.username)
        System.setProperty("spring.balancing-config.datasources.${name}.password", maria.password)
        System.setProperty("spring.balancing-config.datasources.${name}.url", maria.jdbcUrl)
        System.setProperty("spring.balancing-config.datasources.${name}.driver-class-name", maria.jdbcDriverInstance.class.canonicalName)

        String currentOrder = System.getProperty("spring.balancing-config.order")
        String theNewOrder = currentOrder == null ? name : "$currentOrder,$name"
        System.setProperty("spring.balancing-config.order", theNewOrder)
        return maria
    }
}
