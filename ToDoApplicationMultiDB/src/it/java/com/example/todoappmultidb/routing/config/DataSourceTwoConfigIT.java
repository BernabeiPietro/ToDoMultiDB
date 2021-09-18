package com.example.todoappmultidb.routing.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class )
@EnableConfigurationProperties(value = DataSourceTwoConfig.class)
@PropertySource("classpath:application.properties")
public class DataSourceTwoConfigIT {

	@Autowired
	DataSourceTwoConfig data;
	@Test
	public void bindigProperty_test() {
		assertThat(data.getUrl()).isEqualTo("jdbc:mysql://localhost:28012/db_example_2?createDatabaseIfNotExist=true");
		assertThat(data.getPassword()).isEqualTo("password");
		assertThat(data.getUsername()).isEqualTo("administrator");
	}
	
	@Test
	public void connectionDataSource_test() throws SQLException {
		assertThat(data.getDataSource().getConnection().isValid(10)).isTrue();
	}
}
