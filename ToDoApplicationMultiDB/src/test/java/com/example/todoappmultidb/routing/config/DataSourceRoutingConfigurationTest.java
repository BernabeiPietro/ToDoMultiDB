package com.example.todoappmultidb.routing.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.example.todoappmultidb.routing.DataSourceRouter;

@RunWith(MockitoJUnitRunner.class)
public class DataSourceRoutingConfigurationTest {

	@Captor
	private ArgumentCaptor<Map<Object, Object>> mapCaptor;
	@Mock
	DataSourceOneConfig dataSourceOne;
	@Mock
	DataSourceTwoConfig dataSourceTwo;
	@Mock
	DataSourceRouter dataRouter;
	@InjectMocks
	DataSourceRoutingConfiguration config;

	@Test
	public void DataSourceMethodReturnDataSourceRouterInstance_test() {

		DataSource datasource = config.dataSource();
		assertThat(datasource).isEqualTo(dataRouter);

	}

	@Test
	public void DataSourceMethodSetMapOfDataSource() {
		DriverManagerDataSource dataSource1 = new DriverManagerDataSource("localhost", "prova1", "prova1");
		when(dataSourceOne.getDataSource()).thenReturn(dataSource1);
		DriverManagerDataSource dataSource2 = new DriverManagerDataSource("localhost", "prova2", "prova2");
		when(dataSourceTwo.getDataSource()).thenReturn(dataSource2);
		config.dataSource();
		verify(dataRouter).setTargetDataSources(mapCaptor.capture());
		assertThat(mapCaptor.getValue()).containsKey(DataSourceEnum.DATASOURCE_ONE);
		assertThat(mapCaptor.getValue()).containsKey(DataSourceEnum.DATASOURCE_TWO);
		assertThat(mapCaptor.getValue().get(DataSourceEnum.DATASOURCE_ONE)).isEqualTo(dataSource1);
		assertThat(mapCaptor.getValue().get(DataSourceEnum.DATASOURCE_TWO)).isEqualTo(dataSource2);
	}

	@Test
	public void DataSourceMethodSetDefaultDataSource() {
		DriverManagerDataSource dataSource1 = new DriverManagerDataSource("localhost", "prova1", "prova1");
		when(dataSourceOne.getDataSource()).thenReturn(dataSource1);
		config.dataSource();
		verify(dataRouter).setDefaultTargetDataSource(dataSource1);
	}

}
