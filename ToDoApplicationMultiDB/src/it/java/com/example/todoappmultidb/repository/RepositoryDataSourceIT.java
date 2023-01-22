package com.example.todoappmultidb.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.todoappmultidb.model.ToDo;
import com.example.todoappmultidb.model.User;
import com.example.todoappmultidb.routing.DataSourceContextHolder;
import com.example.todoappmultidb.routing.config.DataSourceEnum;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryDataSourceIT {

	@Autowired
	UserRepository userRepository;
	@Autowired
	ToDoRepository todoRepository;
	@Autowired
	DataSourceContextHolder dataContext;
	private User savedUser1;
	private ToDo savedTodo1;
	private User savedUser2;
	private ToDo savedTodo2;
	
	@Before
	public void setup() {
		User toSaveUser = new User(null, new ArrayList<>(), "db1", "db1");
		ToDo toSaveTodo = new ToDo(null, toSaveUser, new HashMap<>(), LocalDateTime.of(2005, 1, 1, 0, 0));
		toSaveTodo.addToDoAction("prova", false);
		toSaveUser.addToDo(toSaveTodo);
		

		dataContext.set(DataSourceEnum.DATASOURCE_ONE);
		savedUser1 = userRepository.save(toSaveUser);
		savedTodo1 = todoRepository.save(toSaveTodo);
		dataContext.set(DataSourceEnum.DATASOURCE_TWO);
	
		User toSaveUser2 = new User(null, new ArrayList<>(), "db2", "db2");
		ToDo toSaveTodo2 = new ToDo(null, toSaveUser, new HashMap<>(), LocalDateTime.of(2015, 2, 2, 0, 0));
		toSaveTodo2.addToDoAction("avorp", false);
		toSaveUser2.addToDo(toSaveTodo);
		savedUser2 = userRepository.save(toSaveUser2);
		savedTodo2 = todoRepository.save(toSaveTodo2);
		
	}
	
	@Test
	public void saveUserAndToDoFirstDB_test() {
		dataContext.set(DataSourceEnum.DATASOURCE_ONE);
		
		Optional<User> findByIdUser = userRepository.findById(savedUser1.getId());
		 List<ToDo> findToDoByUserId = todoRepository.findToDoByUserId(savedUser1);
		assertTrue(findByIdUser.isPresent());
		assertThat(findToDoByUserId).isNotEmpty();
		assertThat(findByIdUser.get()).hasFieldOrPropertyWithValue("id", savedUser1.getId())
				.hasFieldOrPropertyWithValue("email", savedUser1.getEmail())
				.hasFieldOrPropertyWithValue("name", savedUser1.getName());
		assertThat(findByIdUser.get().getToDo()).containsAll(findToDoByUserId);
		dataContext.set(DataSourceEnum.DATASOURCE_TWO);
		assertThat(userRepository.findById(savedUser1.getId())).isNotEqualTo(savedUser1);
		assertThat(todoRepository.findToDoByUserId(savedUser1)).isNotEqualTo(savedTodo1);
	}

	@Test
	public void saveUserAndToDoSecondDB_test() {
		dataContext.set(DataSourceEnum.DATASOURCE_TWO);
	
		Optional<User> findByIdUser = userRepository.findById(savedUser2.getId());
		Optional<ToDo> findByIdTodo = todoRepository.findById(savedTodo2.getId());
		assertTrue(findByIdUser.isPresent());
		assertTrue(findByIdTodo.isPresent());
		assertThat(findByIdUser.get()).hasFieldOrPropertyWithValue("id", savedUser2.getId())
				.hasFieldOrPropertyWithValue("email", savedUser2.getEmail())
				.hasFieldOrPropertyWithValue("name", savedUser2.getName());
		assertThat(findByIdUser.get().getToDo()).contains(findByIdTodo.get());
		dataContext.set(DataSourceEnum.DATASOURCE_ONE);
		assertThat(userRepository.findById(savedUser2.getId())).isNotEqualTo(savedUser2);
		assertThat(todoRepository.findById(savedTodo2.getId())).isNotEqualTo(savedTodo2);
	}

}