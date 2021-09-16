package com.example.todoappmultidb.routing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


 // configuration for FirstDataSource

@Component
@ConfigurationProperties(prefix="datasourceone.datasource")
public class DataSourceOneConfig extends DataSourceConfig {
}