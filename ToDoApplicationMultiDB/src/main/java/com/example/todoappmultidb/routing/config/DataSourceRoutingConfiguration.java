package com.example.todoappmultidb.routing.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.example.todoappmultidb.routing.DataSourceRouter;

@Configuration
@Profile("!repository")
@ComponentScan("com.example.todoappmultidb")
@EnableTransactionManagement
public class DataSourceRoutingConfiguration {

	private static final String SCHEMA_MYSQL_SQL = "schema-mysql.sql";
	@Autowired
	private DataSourceOneConfig dataSourceOne;
	@Autowired
	private DataSourceTwoConfig dataSourceTwo;
	private DataSourceRouter dataRouter = new DataSourceRouter();

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

	@Bean
	public DataSourceInitializer dataSourceOneInitializer() {
		ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
		resourceDatabasePopulator.addScript(new ClassPathResource(SCHEMA_MYSQL_SQL));

		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(dataSourceOne.getDataSource());
		dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
		return dataSourceInitializer;
	}

	@Bean
	public DataSourceInitializer dataSourceTwoInitializer() {
		ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
		resourceDatabasePopulator.addScript(new ClassPathResource(SCHEMA_MYSQL_SQL));
		
		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(dataSourceTwo.getDataSource());
		dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
		return dataSourceInitializer;
	}

}
