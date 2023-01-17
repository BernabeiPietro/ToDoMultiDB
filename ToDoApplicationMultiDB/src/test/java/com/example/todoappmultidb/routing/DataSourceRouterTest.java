package com.example.todoappmultidb.routing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.example.todoappmultidb.routing.config.DataSourceEnum;

@RunWith(MockitoJUnitRunner.class)
public class DataSourceRouterTest {

	@Mock
	DataSourceContextHolder dataContext;
	@InjectMocks
	DataSourceRouter data;
	

	@Test
	public void determineCurrentLookupKey_test() {
		when(dataContext.getDataSource()).thenReturn(DataSourceEnum.DATASOURCE_ONE);
		assertThat(data.determineCurrentLookupKey()).isEqualTo(DataSourceEnum.DATASOURCE_ONE);

	}
	
}
