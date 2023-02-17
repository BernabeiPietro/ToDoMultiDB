package com.example.todoappmultidb.routing;

import com.example.todoappmultidb.routing.config.DataSourceEnum;

public class DataSourceContextHolder {

	private static ThreadLocal<DataSourceEnum> context = new ThreadLocal<>();

	private DataSourceContextHolder() {
		
	}
	public static void set(DataSourceEnum data) {
		context.set(data);

	}

	public static DataSourceEnum getDataSource() {
		return context.get();
	}

	public static void clear() {
		context.remove();
	}

	protected static void setCONTEXT(ThreadLocal<DataSourceEnum> cont) {
		context = cont;
	}

}
