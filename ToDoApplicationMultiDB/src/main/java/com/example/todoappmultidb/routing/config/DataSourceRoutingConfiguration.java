package com.example.todoappmultidb.routing.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.todoappmultidb.routing.DataSourceRouter;

@Configuration
public class DataSourceRoutingConfiguration {
	
	private DataSourceRouter dataRouter= new DataSourceRouter();

	private Map<Object, Object> generetaTargetDataSources() {
		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put(DataSourceEnum.DATASOURCE_ONE, new DataSourceOneConfig().getDataSource());
		targetDataSources.put(DataSourceEnum.DATASOURCE_TWO, new DataSourceTwoConfig().getDataSource());
		return targetDataSources;
	}
	@Bean
	public DataSource dataSource() {
		dataRouter.setTargetDataSources(generetaTargetDataSources());
		dataRouter.setDefaultTargetDataSource(new DataSourceOneConfig().getDataSource());
		return dataRouter;
	}
}
