package com.example.todoappmultidb.routing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;

import com.example.todoappmultidb.routing.config.DataSourceEnum;

public class DataSourceRouterTest {

	private DataSourceRouter data;

	@Test
	public void determineCurrentLookupKey_test() {
		data = new DataSourceRouter();
		DataSourceContextHolder.set(DataSourceEnum.DATASOURCE_ONE);
		assertThat(data.determineCurrentLookupKey()).isEqualTo(DataSourceEnum.DATASOURCE_ONE);

	}
	
}
