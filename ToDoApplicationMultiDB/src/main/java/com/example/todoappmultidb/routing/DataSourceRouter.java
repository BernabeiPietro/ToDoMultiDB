package com.example.todoappmultidb.routing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DataSourceRouter extends AbstractRoutingDataSource {

	@Autowired
	DataSourceContextHolder dataContext;

	@Override
	protected Object determineCurrentLookupKey() {
		return dataContext.getDataSource();
	}

}
