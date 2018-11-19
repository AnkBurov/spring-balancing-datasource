package io.ankburov.spring.balancing.datasource.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class ExtendedDataSourceProperties extends DataSourceProperties {

    private Map<String, String> additionalProperties = new LinkedHashMap<>();
}
