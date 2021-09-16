package com.example.todoappmultidb.routing.config;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DataSourceConfig {

	private String url;
	private String password;
	private String username;

	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public DataSource getDataSource() {
		return new DriverManagerDataSource(url, username, password);
		
	}
	
}