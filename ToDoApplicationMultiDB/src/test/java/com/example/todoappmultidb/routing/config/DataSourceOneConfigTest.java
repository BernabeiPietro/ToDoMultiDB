package com.example.todoappmultidb.routing.config;



import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.todoappmultidb.routing.config.DataSourceOneConfig;

@RunWith(SpringJUnit4ClassRunner.class )
@EnableConfigurationProperties(value = DataSourceOneConfig.class)
@TestPropertySource("classpath:server.properties")
public class DataSourceOneConfigTest {
	@Autowired
	private DataSourceOneConfig data;
	@Test
	public void datasourceOneConfigBindingValue_test() {
		assertThat(data.getUrl()).isEqualTo("localhost");
		assertThat(data.getPassword()).isEqualTo("password");
		assertThat(data.getUsername()).isEqualTo("root");
		}
}
