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