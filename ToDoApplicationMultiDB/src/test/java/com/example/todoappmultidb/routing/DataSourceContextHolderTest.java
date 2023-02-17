package com.example.todoappmultidb.routing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.example.todoappmultidb.routing.config.DataSourceEnum;

@RunWith(MockitoJUnitRunner.class)
public class DataSourceContextHolderTest {

	@Spy
	private ThreadLocal<DataSourceEnum> CONTEXT;

	@Before
	public void setup() {
		DataSourceContextHolder.setCONTEXT(CONTEXT);
	}

	@Test
	public void setDataSourceOne_test() {
		DataSourceContextHolder.set(DataSourceEnum.DATASOURCE_ONE);
		verify(CONTEXT).set(DataSourceEnum.DATASOURCE_ONE);
	}

	@Test
	public void setDataSourceTwo_test() {
		DataSourceContextHolder.set(DataSourceEnum.DATASOURCE_TWO);
		verify(CONTEXT).set(DataSourceEnum.DATASOURCE_TWO);
	}

	@Test
	public void clear_test() {
		CONTEXT.set(DataSourceEnum.DATASOURCE_ONE);
		DataSourceContextHolder.clear();
		verify(CONTEXT).remove();
		assertThat(CONTEXT.get()).isNull();
	}

	@Test
	public void getDataSource_test() {
		CONTEXT.set(DataSourceEnum.DATASOURCE_ONE);
		assertThat(DataSourceContextHolder.getDataSource()).isEqualTo(DataSourceEnum.DATASOURCE_ONE);
	}

}
