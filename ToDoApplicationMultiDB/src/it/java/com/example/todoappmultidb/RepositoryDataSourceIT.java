package com.example.todoappmultidb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.todoappmultidb.model.ToDo;
import com.example.todoappmultidb.model.User;
import com.example.todoappmultidb.repository.ToDoRepository;
import com.example.todoappmultidb.repository.UserRepository;
import com.example.todoappmultidb.routing.DataSourceContextHolder;
import com.example.todoappmultidb.routing.config.DataSourceEnum;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryDataSourceIT {

	@Autowired
	UserRepository userRepository;
	@Autowired
	ToDoRepository todoRepository;

	@Before
	public void setup() {
		DataSourceContextHolder.set(DataSourceEnum.DATASOURCE_ONE);
		userRepository.deleteAll();
		DataSourceContextHolder.set(DataSourceEnum.DATASOURCE_TWO);
		userRepository.deleteAll();

	}

	@Test
	public void saveUserAndToDoFirstDB_test() {
		DataSourceContextHolder.set(DataSourceEnum.DATASOURCE_ONE);
		User toSaveUser = new User(null, new ArrayList<>(), "db1", "db1");
		ToDo toSaveTodo = new ToDo(null, toSaveUser, new HashMap<>(), LocalDateTime.of(2005, 1, 1, 0, 0));
		toSaveTodo.addToDoAction("prova", false);
		toSaveUser.addToDo(toSaveTodo);
		User savedUser = userRepository.save(toSaveUser);
		ToDo savedTodo = todoRepository.save(toSaveTodo);
		Optional<User> findByIdUser = userRepository.findById(savedUser.getId());
		 List<ToDo> findToDoByUserId = todoRepository.findToDoByUserId(savedUser);
		assertTrue(findByIdUser.isPresent());
		assertThat(findToDoByUserId).isNotEmpty();
		assertThat(findByIdUser.get()).hasFieldOrPropertyWithValue("id", savedUser.getId())
				.hasFieldOrPropertyWithValue("email", savedUser.getEmail())
				.hasFieldOrPropertyWithValue("name", savedUser.getName());
		assertThat(findByIdUser.get().getToDo()).containsAll(findToDoByUserId);
		DataSourceContextHolder.set(DataSourceEnum.DATASOURCE_TWO);
		assertThat(userRepository.findById(savedUser.getId())).isEmpty();
		assertThat(todoRepository.findToDoByUserId(savedUser)).isEmpty();
	}

	@Test
	public void saveUserAndToDoSecondDB_test() {
		DataSourceContextHolder.set(DataSourceEnum.DATASOURCE_TWO);
		User toSaveUser = new User(null, new ArrayList<>(), "db1", "db1");
		ToDo toSaveTodo = new ToDo(null, toSaveUser, new HashMap<>(), LocalDateTime.of(2005, 1, 1, 0, 0));
		toSaveTodo.addToDoAction("prova", false);
		toSaveUser.addToDo(toSaveTodo);
		User savedUser = userRepository.save(toSaveUser);
		ToDo savedTodo = todoRepository.save(toSaveTodo);
		Optional<User> findByIdUser = userRepository.findById(savedUser.getId());
		Optional<ToDo> findByIdTodo = todoRepository.findById(savedTodo.getId());
		assertTrue(findByIdUser.isPresent());
		assertTrue(findByIdTodo.isPresent());
		assertThat(findByIdUser.get()).hasFieldOrPropertyWithValue("id", savedUser.getId())
				.hasFieldOrPropertyWithValue("email", savedUser.getEmail())
				.hasFieldOrPropertyWithValue("name", savedUser.getName());
		assertThat(findByIdUser.get().getToDo()).contains(findByIdTodo.get());
		DataSourceContextHolder.set(DataSourceEnum.DATASOURCE_ONE);
		assertThat(userRepository.findById(savedUser.getId())).isEmpty();
		assertThat(todoRepository.findById(savedTodo.getId())).isEmpty();
	}

}