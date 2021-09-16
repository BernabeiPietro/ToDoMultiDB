package com.example.todoappmultidb.routing.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.DriverManager;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class )
public class DataSourceConfigTest {

	DataSourceConfig config;

	@Test
	public void getDataSource_test() {
		config=new DataSourceConfig();
		config.setUsername("prova");
		config.setUrl("localhost");
		config.setPassword("password");
		DataSource dataSource = config.getDataSource();
		assertThat(dataSource).hasFieldOrPropertyWithValue("url", "localhost");
		assertThat(dataSource).hasFieldOrPropertyWithValue("username", "prova");
		assertThat(dataSource).hasFieldOrPropertyWithValue("password", "password");
		}

}
