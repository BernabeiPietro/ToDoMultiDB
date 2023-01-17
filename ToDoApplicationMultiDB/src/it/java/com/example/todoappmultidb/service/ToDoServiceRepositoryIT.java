package com.example.todoappmultidb.service;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.HashMap;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.example.todoappmultidb.model.ToDo;
import com.example.todoappmultidb.model.User;
import com.example.todoappmultidb.routing.DataSourceContextHolder;
import com.example.todoappmultidb.routing.config.DataSourceEnum;
import com.example.todoappmultidb.routing.config.DataSourceRoutingConfiguration;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=DataSourceRoutingConfiguration.class)
@EnableConfigurationProperties
@PropertySource("classpath:application.properties")
@SpringBootTest
@Transactional
public class ToDoServiceRepositoryIT {

	@Autowired
	private ToDoService todoService;
	@Autowired
	private UserService userService;
	
	JdbcTemplate jdbcTemplate;
	int todoNRowDOne;
	int userNRowDOne;
	int todoNRowDTwo;
	int userNRowDTwo;

	@Autowired
	void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@BeforeTransaction
	public void verifyInitialDatabaseState() {
		// logic to verify the initial state before a transaction is started
		DataSourceContextHolder.set(DataSourceEnum.DATASOURCE_ONE);
		todoNRowDOne = JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "to_do");
		userNRowDOne = JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "user");
		DataSourceContextHolder.set(DataSourceEnum.DATASOURCE_TWO);
		todoNRowDTwo = JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "to_do");
		userNRowDTwo = JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "user");

	}
	
	
	  
	/*
	 * @Test // overrides the class-level defaultRollback setting public void
	 * insertToDoUserWithinTransaction() { // logic which uses the test data and
	 * modifies database state
	 * DataSourceContextHolder.set(DataSourceEnum.DATASOURCE_ONE); User
	 * user=userService.insertNewUser(new User(null,null, "prova", "prova"));
	 * todoService.save(new ToDo(null, user, new HashMap<>(),LocalDateTime.of(2005,
	 * 1, 1, 0, 0))); assertThat(JdbcTestUtils.countRowsInTable(this.jdbcTemplate,
	 * "to_do")).isEqualTo(todoNRowDOne+1);
	 * assertThat(JdbcTestUtils.countRowsInTable(this.jdbcTemplate,
	 * "user")).isEqualTo(userNRowDOne+1); }
	 */
	  @Test // overrides the class-level defaultRollback setting
	  public void insertToDoUserWithinTransaction() { 
		  // logic which uses the test data and modifies database state 
		  DataSourceContextHolder.set(DataSourceEnum.DATASOURCE_ONE);
		  User user= new User(null,null, "prova", "prova");
		  User user_one=userService.insertNewUser(user);
		  DataSourceContextHolder.set(DataSourceEnum.DATASOURCE_TWO);
		  todoService.save(new ToDo(null, user, new HashMap<>(),LocalDateTime.of(2005, 1, 1, 0, 0)));
		  assertThat(JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "to_do")).isEqualTo(todoNRowDOne+1);
		  assertThat(JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "user")).isEqualTo(userNRowDOne+1);
	  }
	  
	
	 
}
