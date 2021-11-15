package com.example.todoappmultidb.routing;


import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.todoappmultidb.routing.config.DataSourceEnum;
import com.example.todoappmultidb.routing.config.DataSourceRoutingConfiguration;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=DataSourceRoutingConfiguration.class)
@EnableConfigurationProperties
@PropertySource("classpath:application.properties")
public class DataSourceIT {

	@Autowired
	private DataSource data;
	@Test
	public void dataSourceConnectionOne_test() throws SQLException {
		DataSourceContextHolder.set(DataSourceEnum.DATASOURCE_ONE);
		assertThat(data.getConnection().isValid(10)).isTrue();
		assertThat(data.getConnection().getMetaData().getURL()).isEqualTo("jdbc:mysql://localhost:28011/db_example_1?createDatabaseIfNotExist=true");
		DataSourceContextHolder.clear();
	}
	@Test
	public void dataSourceConnectionTwo_test() throws SQLException {
		DataSourceContextHolder.set(DataSourceEnum.DATASOURCE_TWO);
		assertThat(data.getConnection().isValid(10)).isTrue();
		assertThat(data.getConnection().getMetaData().getURL()).isEqualTo("jdbc:mysql://localhost:28012/db_example_2?createDatabaseIfNotExist=true");
		DataSourceContextHolder.clear();
	}

}
