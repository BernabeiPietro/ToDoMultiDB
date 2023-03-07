package com.example.todoappmultidb.routing.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableConfigurationProperties(value = DataSourceTwoConfig.class)
@TestPropertySource("classpath:server.properties")
public class DataSourceTwoConfigTest {

	@Autowired
	private DataSourceTwoConfig data;

	@Test
	public void datasourceOneConfigBindingValue_test() {
		assertThat(data.getUrl()).isEqualTo("localhost");
		assertThat(data.getPassword()).isEqualTo("password");
		assertThat(data.getUsername()).isEqualTo("root");
	}

}
