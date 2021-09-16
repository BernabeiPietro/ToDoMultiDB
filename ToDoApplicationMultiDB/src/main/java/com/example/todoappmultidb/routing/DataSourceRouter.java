package com.example.todoappmultidb.routing;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DataSourceRouter extends AbstractRoutingDataSource {

	protected Object determineCurrentLookupKey() {
		return DataSourceContextHolder.getDataSource();
	}

}
