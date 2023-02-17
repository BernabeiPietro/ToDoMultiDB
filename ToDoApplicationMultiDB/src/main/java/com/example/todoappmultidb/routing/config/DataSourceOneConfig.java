package com.example.todoappmultidb.routing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;




@Component
@ConfigurationProperties("datasourceone.datasource")
public class DataSourceOneConfig extends DataSourceConfig {
	public DataSourceOneConfig() {
		super();
	}
}