package com.example.todoappmultidb.routing.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

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

	@Mock
	ResourceDatabasePopulator resourceDatabasePopulator;
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
		assertThat(mapCaptor.getValue()).containsEntry(DataSourceEnum.DATASOURCE_ONE, dataSource1);
		assertThat(mapCaptor.getValue()).containsEntry(DataSourceEnum.DATASOURCE_TWO, dataSource2);
	}

	@Test
	public void DataSourceMethodSetDefaultDataSource() {
		DriverManagerDataSource dataSource1 = new DriverManagerDataSource("localhost", "prova1", "prova1");
		when(dataSourceOne.getDataSource()).thenReturn(dataSource1);
		config.dataSource();
		verify(dataRouter).setDefaultTargetDataSource(dataSource1);
	}

	@Test
	public void test_dataSourceOneInitializer() throws SQLException {
		DataSource datasource = Mockito.mock(DataSource.class);
		when(dataSourceOne.getDataSource()).thenReturn(datasource);
		config.dataSourceOneInitializer();
		verify(resourceDatabasePopulator).addScript(new ClassPathResource("schema-mysql.sql"));
		verify(dataSourceOne).getDataSource();
	}

	@Test
	public void test_dataSourceTwoInitializer() throws SQLException {
		DataSource datasource = Mockito.mock(DataSource.class);
		when(dataSourceTwo.getDataSource()).thenReturn(datasource);
		config.dataSourceTwoInitializer();
		verify(resourceDatabasePopulator).addScript(new ClassPathResource("schema-mysql.sql"));
		verify(dataSourceTwo).getDataSource();
	}

	@Test
	public void test_initResourceDataPopulator() {
		assertThat(config.plainResourceDataPopulator()).isExactlyInstanceOf(ResourceDatabasePopulator.class);

	}

}
