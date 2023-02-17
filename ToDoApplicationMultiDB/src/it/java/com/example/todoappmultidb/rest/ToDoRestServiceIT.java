package com.example.todoappmultidb.rest;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.todoappmultidb.model.ToDo;
import com.example.todoappmultidb.model.User;
import com.example.todoappmultidb.model.dto.ToDoDTO;
import com.example.todoappmultidb.model.dto.UserDTO;
import com.example.todoappmultidb.repository.ToDoRepository;
import com.example.todoappmultidb.repository.UserRepository;
import com.example.todoappmultidb.service.UserService;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ToDoRestServiceIT {

	@Autowired
	UserService userService;
	@Autowired
	ToDoRepository todoRepository;
	@Autowired
	UserRepository userRepository;
	@LocalServerPort
	private int port;
	
	@Before
	public void setup() {
	RestAssured.port = port;
	// always start with an empty database
	userService.setContext(1);
	userRepository.deleteAll();
	userRepository.flush();
	userService.setContext(2);
	userRepository.deleteAll();
	userRepository.flush();
	}
	
	
	@Test
	public void testPost_NewTodo_db1() throws Exception {
		userService.setContext(1);
		User saved=userRepository.save(new User(null, "1emon", "1emon"));
		Response response = given().
				contentType(MediaType.APPLICATION_JSON_VALUE).
				body(new ToDoDTO(null,saved.getId(),new HashMap<>(),LocalDateTime.of(2000, 5, 13, 1, 1, 1) )).
				when().
				post("/api/todo/1/new");
		ToDoDTO todoSaved =response.getBody().as(ToDoDTO.class);
		userService.setContext(1);
		assertThat(new ToDoDTO(todoRepository.findById(todoSaved.getId()).get())).isEqualTo(todoSaved);
		userService.setContext(2);
		assertThat(todoRepository.findById(todoSaved.getId()).isPresent()).isFalse();
	}
	@Test
	public void testPost_NewTodo_db2() throws Exception {
		userService.setContext(2);
		User saved=userRepository.save(new User(null, "2emon", "2emon"));
		Response response = given().
				contentType(MediaType.APPLICATION_JSON_VALUE).
				body(new ToDoDTO(null,saved.getId(),new HashMap<>(),LocalDateTime.of(2000, 5, 13, 1, 1, 1) )).
				when().
				post("/api/todo/2/new");
		ToDoDTO todoSaved =response.getBody().as(ToDoDTO.class);
		userService.setContext(2);
		assertThat(new ToDoDTO(todoRepository.findById(todoSaved.getId()).get())).isEqualTo(todoSaved);
		userService.setContext(1);
		assertThat(todoRepository.findById(todoSaved.getId()).isPresent()).isFalse();
	}
	
	@Test
	public void testPut_UpdateUser_db2() {
		userService.setContext(2);
		User userSaved=userRepository.save(new User(1L, "2emon", "2emon"));
		ToDo todoSaved=todoRepository.save(new ToDo(null, userSaved, LocalDateTime.of(2000, 5, 13, 1, 1, 1)));
		Response response = given().
				contentType(MediaType.APPLICATION_JSON_VALUE).
				body(new ToDoDTO(null,userSaved.getId(),new HashMap<>(),LocalDateTime.of(2000, 5, 13, 1, 1, 1) )).
				when().
				put("/api/todo/2/update/"+todoSaved.getId());
		ToDoDTO todo =response.getBody().as(ToDoDTO.class);
		userService.setContext(2);
		assertThat(new ToDoDTO(todoRepository.findById(todoSaved.getId()).get())).isEqualTo(todo);
		userService.setContext(1);
		assertThat(todoRepository.findById(todoSaved.getId()).isPresent()).isFalse();
		}
	@Test
	public void testPut_UpdateUser_db1() {
		userService.setContext(1);
		User userSaved=userRepository.save(new User(1L, "1emon", "1emon"));
		ToDo todoSaved=todoRepository.save(new ToDo(null, userSaved, LocalDateTime.of(2000, 5, 13, 1, 1, 1)));
		Response response = given().
				contentType(MediaType.APPLICATION_JSON_VALUE).
				body(new ToDoDTO(null,userSaved.getId(),new HashMap<>(),LocalDateTime.of(2000, 5, 13, 1, 1, 1) )).
				when().
				put("/api/todo/1/update/"+todoSaved.getId());
		ToDoDTO todo =response.getBody().as(ToDoDTO.class);
		userService.setContext(1);
		assertThat(new ToDoDTO(todoRepository.findById(todoSaved.getId()).get())).isEqualTo(todo);
		userService.setContext(2);
		assertThat(todoRepository.findById(todoSaved.getId()).isPresent()).isFalse();
		}


}
