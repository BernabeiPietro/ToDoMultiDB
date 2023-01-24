package com.example.todoappmultidb.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
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
import com.example.todoappmultidb.model.dto.ToDoDTO;
import com.example.todoappmultidb.model.dto.UserDTO;
import com.example.todoappmultidb.repository.ToDoRepository;
import com.example.todoappmultidb.repository.UserRepository;
import com.example.todoappmultidb.routing.DataSourceContextHolder;
import com.example.todoappmultidb.routing.config.DataSourceEnum;
import com.example.todoappmultidb.routing.config.DataSourceRoutingConfiguration;

import javassist.NotFoundException;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DataSourceRoutingConfiguration.class)
@EnableConfigurationProperties
@PropertySource("classpath:application.properties")
@SpringBootTest
public class ServiceRepositoryIT {

	@Autowired
	private ToDoService todoService;
	@Autowired
	private UserService userService;
	@Autowired
	private ToDoRepository todoRepository;
	@Autowired
	private UserRepository userRepository;

	
	
	@Test
	public void findToDoUserWithinTransaction() throws NotFoundException {

		//setup DB1
		userService.setContext(1);
		User toSaveUser = new User(null, new ArrayList<>(), "db1", "db1");
		ToDo toSaveTodo = new ToDo(null, toSaveUser, new HashMap<>(), LocalDateTime.of(2005, 1, 1, 0, 0));
		toSaveTodo.addToDoAction("prova", false);
		toSaveUser.addToDo(toSaveTodo);
		User savedUser1 = userRepository.save(toSaveUser);
		ToDo savedTodo1 = todoRepository.save(toSaveTodo);

		//setup DB2
		userService.setContext(2);
		User toSaveUser2 = new User(null, new ArrayList<>(), "db2", "db2");
		ToDo toSaveTodo2 = new ToDo(null, toSaveUser2, new HashMap<>(), LocalDateTime.of(2015, 2, 2, 0, 0));
		toSaveTodo2.addToDoAction("avorp", true);
		toSaveUser2.addToDo(toSaveTodo2);
		User savedUser2 = userRepository.save(toSaveUser2);
		ToDo savedTodo2 = todoRepository.save(toSaveTodo2);
		
		userService.setContext(1);
		assertThat(userService.getUserById(savedUser1.getId()))
			.isEqualTo(new UserDTO(savedUser1));
		assertThat(todoService.findByIdDTO(savedTodo1.getId())).isEqualTo(new ToDoDTO(savedTodo1));
		assertThat(userService.getAllUser()).doesNotContain(new UserDTO(savedUser2));
		assertThat(todoService.findAll()).doesNotContain(new ToDoDTO(savedTodo2));
		
		userService.setContext(2);
		assertThat(userService.getUserById(savedUser2.getId())).isEqualTo(new UserDTO(savedUser2));
		assertThat(todoService.findByIdDTO(savedTodo2.getId())).isEqualTo(new ToDoDTO(savedTodo2));
		assertThat(userService.getAllUser()).doesNotContain(new UserDTO(savedUser1));
		assertThat(todoService.findAll()).doesNotContain(new ToDoDTO(savedTodo1));

	}

	@Test
	public void insertToDoUserWithinTransaction() throws NotFoundException {

		userService.setContext(1);
		UserDTO user_one_db = new UserDTO(null, "db1_int", "db1_int");
		user_one_db = userService.insertNewUser(user_one_db);
		
		ToDoDTO todo_one_db = new ToDoDTO(null, user_one_db.getId(),new HashMap<>(), LocalDateTime.of(2015, 6, 2, 2, 4));
		todo_one_db.addToDoAction("prova1_int", false);
		todo_one_db = todoService.save(todo_one_db);
		
		userService.setContext(2);
		UserDTO user_two_db = new UserDTO(null, "db2_int", "db2_int");
		user_two_db = userService.insertNewUser(user_two_db);

		ToDoDTO todo_two_db = new ToDoDTO(null, user_two_db.getId(), new HashMap<>(), LocalDateTime.of(2012, 4, 3, 1, 1));
		todo_two_db.addToDoAction("prova1_int", false);
		todo_two_db = todoService.save(todo_two_db);

		userService.setContext(1);
		assertThat(userService.getUserById(user_one_db.getId())).isEqualTo(user_one_db);
		assertThat(todoService.findByIdDTO(todo_one_db.getId())).isEqualTo(todo_one_db);

		userService.setContext(2);
		assertThat(userService.getUserById(user_two_db.getId())).isEqualTo(user_two_db);
		assertThat(todoService.findByIdDTO(todo_two_db.getId())).isEqualTo(todo_two_db);
	}
	@Test
	public void verifyRollBack() {
		UserDTO nullUser=new UserDTO(null,null,null);
		ToDoDTO nullToDo=new ToDoDTO(null, null, null, null);
		
		userService.setContext(1);
		int todoQta1= todoRepository.findAll().size();
		int userQta1= userRepository.findAll().size();

		userService.setContext(2);
		int todoQta2= todoRepository.findAll().size();
		int userQta2= userRepository.findAll().size();

		userService.setContext(1);
		assertThrows(IllegalArgumentException.class,()->userService.insertNewUser(nullUser));
		assertThrows(IllegalArgumentException.class,()->todoService.save(nullToDo));
		assertThat(todoRepository.findAll()).hasSize(todoQta1);
		assertThat(userRepository.findAll()).hasSize(userQta1);

		userService.setContext(2);
		assertThrows(IllegalArgumentException.class,()->userService.insertNewUser(nullUser));
		assertThrows(IllegalArgumentException.class,()->todoService.save(nullToDo));
		assertThat(todoRepository.findAll()).hasSize(todoQta2);
		assertThat(userRepository.findAll()).hasSize(userQta2);
	}


}
