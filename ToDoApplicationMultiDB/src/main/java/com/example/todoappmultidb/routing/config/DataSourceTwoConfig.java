package com.example.todoappmultidb.routing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "datasourcetwo.datasource")
public class DataSourceTwoConfig extends DataSourceConfig{
	
}