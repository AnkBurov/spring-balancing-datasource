# Spring-balancing-datasource
[ ![Download](https://api.bintray.com/packages/ankburov/maven/spring-balancing-datasource/images/download.svg) ](https://bintray.com/ankburov/maven/spring-balancing-datasource/_latestVersion)
[ ![CircleCI](https://circleci.com/gh/AnkBurov/spring-balancing-datasource/tree/master.svg?style=shield) ](https://circleci.com/gh/AnkBurov/spring-balancing-datasource/tree/master)  

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

## Download
### Gradle
```groovy
repositories {
    jcenter()
}

dependencies {
    compile "io.ankburov:spring-balancing-datasource:<version>"
}
```

## Usage

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

## Ignoring datasources

Sometimes in strict deployment environments number of used properties is rigid (for example in Ansible's deployments
where there is one or several playbooks and a number of different environments around them - some with one datasource, some with 
many). To overcome this datasources can be ignored. It means that they will be ignored on an initialization of 
`BalancingDataSource` and won't be created as `javax.sql.DataSource` instances. 

To do that, url property of ignoring datasources should have value `IGNORE`:
```yaml
spring:
    balancing-dataSources-config:
        data-sources:
            main_oracle:
                url: jdbc:oracle:thin:@localhost:1521:xe
                username: system
                password: oracle
                name: main_oracle
            ignored_datasource:
                url: IGNORE
        data-sources-order: "main_oracle"
```
With such configuration only one instance of `javax.sql.DataSource` with the name `main_oracle` will be created