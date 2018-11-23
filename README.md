# Spring-balancing-datasource
[![](https://jitpack.io/v/AnkBurov/spring-balancing-datasource.svg)](https://jitpack.io/#AnkBurov/spring-balancing-datasource)

Redundant abstraction over multiple datasources. 
Gives functionality like MariaDB's`jdbc:mysql:replication//.....` 
or Oracle's `jdbc:oracle:thin:@(DESCRIPTION = (FAILOVER=on)` to any databases and datasources
without messing with the availability definition of each database vendor. 

Balancing datasources are defined as system properties under `spring.balancing-dataSources-config`
property section. 

By default each declared datasource is created as HikariCP datasource. If different
`javax.sql.DataSource` implementation is needed, then own bean implementation
of the interface `io.ankburov.spring.balancing.datasource.factory.DataSourceFactory`
should be used. 

# Download
## Gradle
### Bintray
```groovy
repositories {
    jcenter()
}

dependencies {
    compile "io.ankburov:spring-balancing-datasource<version>"
}
```

### JitPack
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compile "com.github.AnkBurov:spring-balancing-datasource:<version>"
}
```

# Usage

Add `@EnableBalancingDataSource` annotation to your Spring boot configuration:
```java
@Configuration
@EnableBalancingDataSource
public class JdbcAdapterConfiguration {}
```

Define minimal configuration in properties. All available properties are defined in `BalancingDataSourceConfiguration`
or in generated `spring-configuration-metadata.json`

`application.yaml` example:
```yaml
spring:
    balancing-dataSources-config:
        data-sources:
            main_oracle:
                url: jdbc:oracle:thin:@localhost:1521:xe
                username: system
                password: oracle
                name: whatever
            standby_oracle:
                url: jdbc:oracle:thin:@localhost:1525:xe
                username: system
                password: oracle
                name: jdbc-adapter-local-test
        data-sources-order: "main_oracle,standby_oracle"
```
After that all JDBC connections from `dataSource` bean with an implementation as `BalancingDataSource` will be balanced across 
defined datasources according to used `BalancingStrategy` - in failover manner or load-balancing one. 