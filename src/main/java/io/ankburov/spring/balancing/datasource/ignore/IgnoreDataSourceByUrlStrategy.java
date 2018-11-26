package io.ankburov.spring.balancing.datasource.ignore;

import io.ankburov.spring.balancing.datasource.property.ExtendedDataSourceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import java.util.Optional;

@RequiredArgsConstructor
public class IgnoreDataSourceByUrlStrategy implements IgnoreDataSourceStrategy {

    @Override
    public boolean ignore(ExtendedDataSourceProperties dataSourceProperties) {
        return Optional.ofNullable(dataSourceProperties)
                       .map(DataSourceProperties::getUrl)
                       .map(IGNORE::equalsIgnoreCase)
                       .orElse(false);
    }
}
