package com.example.todoappmultidb.routing;

import org.springframework.stereotype.Component;

import com.example.todoappmultidb.routing.config.DataSourceEnum;

@Component
public class DataSourceContextHolder {

	private static ThreadLocal<DataSourceEnum> context = new ThreadLocal<>();

	private DataSourceContextHolder() {
		
	}
	public void set(DataSourceEnum data) {
		context.set(data);

	}

	public DataSourceEnum getDataSource() {
		return context.get();
	}

	public void clear() {
		context.remove();
	}
	
	protected static void setCONTEXT(ThreadLocal<DataSourceEnum> cont) {
		context = cont;
	}
	
	public DataSourceEnum setContext(int ctx) {
		if (ctx <= 1)
			this.set(DataSourceEnum.values()[0]);
		else
			this.set(DataSourceEnum.values()[(ctx-1) % 2]);
		return this.getDataSource();
	}
}
