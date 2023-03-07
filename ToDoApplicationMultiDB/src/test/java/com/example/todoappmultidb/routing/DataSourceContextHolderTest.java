package com.example.todoappmultidb.routing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.example.todoappmultidb.routing.config.DataSourceEnum;

@RunWith(MockitoJUnitRunner.class)
public class DataSourceContextHolderTest {

	@Spy
	ThreadLocal<DataSourceEnum> CONTEXT;
	@InjectMocks
	DataSourceContextHolder dataContext;

	@Before
	public void setup() {
		dataContext.setCONTEXT(CONTEXT);
	}

	@Test
	public void setDataSourceOne_test() {
		dataContext.set(DataSourceEnum.DATASOURCE_ONE);
		verify(CONTEXT).set(DataSourceEnum.DATASOURCE_ONE);
	}

	@Test
	public void setDataSourceTwo_test() {
		dataContext.set(DataSourceEnum.DATASOURCE_TWO);
		verify(CONTEXT).set(DataSourceEnum.DATASOURCE_TWO);
	}

	@Test
	public void clear_test() {
		CONTEXT.set(DataSourceEnum.DATASOURCE_ONE);
		dataContext.clear();
		verify(CONTEXT).remove();
		assertThat(CONTEXT.get()).isNull();
	}

	@Test
	public void getDataSource_test() {
		CONTEXT.set(DataSourceEnum.DATASOURCE_ONE);
		assertThat(dataContext.getDataSource()).isEqualTo(DataSourceEnum.DATASOURCE_ONE);
	}

	@Test
	public void test_setContext_one() {
		assertThat(dataContext.setDatabase(1)).isEqualTo(DataSourceEnum.DATASOURCE_ONE);
	}

	@Test
	public void test_setContext_two() {
		assertThat(dataContext.setDatabase(2)).isEqualTo(DataSourceEnum.DATASOURCE_TWO);
	}

	@Test
	public void test_setContext_callDataContext() {
		dataContext.setDatabase(1);
		verify(CONTEXT).set(any(DataSourceEnum.class));
	}

	@Test
	public void test_setContext_outside_range() {
		assertThat(dataContext.setDatabase(3)).isEqualTo(DataSourceEnum.DATASOURCE_ONE);

	}

	@Test
	public void test_setContext_under_range() {
		assertThat(dataContext.setDatabase(0)).isEqualTo(DataSourceEnum.DATASOURCE_ONE);

	}
}
