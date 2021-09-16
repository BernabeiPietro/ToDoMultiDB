package com.example.todoappmultidb.routing.config;

import com.example.todoappmultidb.routing.DataSourceRouter;
import com.example.todoappmultidb.routing.config.DataSourceRoutingConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataSourceRoutingConfigurationTest {

	@Captor
	private ArgumentCaptor<Map<Object,Object>> mapCaptor;
	@Captor
	private ArgumentCaptor<DataSource> datasourceCaptor;
	@Mock
	DataSourceRouter dataRouter;
	@InjectMocks
	DataSourceRoutingConfiguration config;

	@Test
	public void DataSourceMethodReturnDataSourceRouterInstance_test(){
		
		DataSource datasource = config.dataSource();
		assertThat(datasource).isEqualTo(dataRouter);
		
	}
	@Test
	public void DataSourceMethodSetMapOfDataSource() {
		DataSource datasource = config.dataSource();
		verify(dataRouter).setTargetDataSources(mapCaptor.capture());
		assertThat(mapCaptor.getValue()).containsKey(DataSourceEnum.DATASOURCE_ONE);
		assertThat(mapCaptor.getValue()).containsKey(DataSourceEnum.DATASOURCE_TWO);
		assertThat(mapCaptor.getValue().get(DataSourceEnum.DATASOURCE_ONE)).isInstanceOf(DriverManagerDataSource.class);
		assertThat(mapCaptor.getValue().get(DataSourceEnum.DATASOURCE_TWO)).isInstanceOf(DriverManagerDataSource.class);
	}
	@Test
	public void DataSourceMethodSetDefaultDataSource() {
		DataSource datasource = config.dataSource();
		verify(dataRouter).setDefaultTargetDataSource(datasourceCaptor.capture());
		assertThat(datasourceCaptor.getValue()).isInstanceOf(DriverManagerDataSource.class);
	}
	

}
