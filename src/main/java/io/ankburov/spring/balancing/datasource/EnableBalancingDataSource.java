package io.ankburov.spring.balancing.datasource;

import io.ankburov.spring.balancing.datasource.config.BalancingDataSourceConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Configuration
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(BalancingDataSourceConfiguration.class)
public @interface EnableBalancingDataSource {
}
