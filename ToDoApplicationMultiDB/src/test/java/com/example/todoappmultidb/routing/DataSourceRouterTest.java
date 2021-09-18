package com.example.todoappmultidb.routing;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

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
