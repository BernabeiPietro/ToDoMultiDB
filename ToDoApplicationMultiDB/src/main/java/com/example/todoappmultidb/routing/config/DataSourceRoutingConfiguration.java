package com.example.todoappmultidb.routing.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.example.todoappmultidb.routing.DataSourceRouter;

@Configuration
@ComponentScan("com.example.todoappmultidb.routing")
public class DataSourceRoutingConfiguration {

	@Autowired
	private DataSourceOneConfig dataSourceOne;
	@Autowired
	private DataSourceTwoConfig dataSourceTwo;
	private DataSourceRouter dataRouter= new DataSourceRouter();

	private Map<Object, Object> generetaTargetDataSources() {
		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put(DataSourceEnum.DATASOURCE_ONE, dataSourceOne.getDataSource());
		targetDataSources.put(DataSourceEnum.DATASOURCE_TWO, dataSourceTwo.getDataSource());
		return targetDataSources;
	}

	@Bean
	public DataSource dataSource() {

		dataRouter.setTargetDataSources(generetaTargetDataSources());
		dataRouter.setDefaultTargetDataSource(dataSourceOne.getDataSource());
		return dataRouter;
	}
	
}
