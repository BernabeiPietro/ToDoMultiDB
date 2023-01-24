package com.example.todoappmultidb.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hamcrest.core.IsNot;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.example.todoappmultidb.model.ToDo;
import com.example.todoappmultidb.model.User;
import com.example.todoappmultidb.model.dto.ToDoDTO;
import com.example.todoappmultidb.model.dto.UserDTO;
import com.example.todoappmultidb.repository.ToDoRepository;
import com.example.todoappmultidb.repository.UserRepository;
import com.example.todoappmultidb.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@RunWith(SpringRunner.class)
@EnableConfigurationProperties
@SpringBootTest
@AutoConfigureMockMvc
public class RestServiceIT {
	@Autowired
	MockMvc mvc;
	ObjectMapper objMapper;

	@Autowired
	UserService userService;
	@Autowired
	ToDoRepository todoRepository;
	@Autowired
	UserRepository userRepository;

	@Before
	public void setup()
	{

		objMapper = new ObjectMapper();
		objMapper.registerModule(new JavaTimeModule());
		objMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}
	
	@Test
	public void testPost_ToDo() throws JsonProcessingException, Exception {
		userService.setContext(1);
		User toSaveUser = new User(null, new ArrayList<>(), "db1", "db1");
		User savedUser1 = userRepository.save(toSaveUser);
		userService.setContext(2);
		User toSaveUser2 = new User(null, new ArrayList<>(), "db2", "db2");
		User savedUser2 = userRepository.save(toSaveUser2);
		HashMap<String, Boolean> actions = new HashMap<String, Boolean>();
		actions.put("first", false);
		actions.put("second",true);
		ToDoDTO todo1 = new ToDoDTO(null, savedUser1.getId(), actions, LocalDateTime.of(2000, 5, 13, 1, 1, 1));
		ToDoDTO todo2 = new ToDoDTO(null, savedUser2.getId(), actions, LocalDateTime.of(2000, 5, 13, 1, 1, 1));

		
		this.mvc.perform(post("/api/todo/1/new").content(objMapper.writeValueAsString(todo1))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isCreated()).andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.date", is(LocalDateTime.of(2000, 5, 13, 1, 1, 1).toString())))
				.andExpect(jsonPath("$.idOfUser", is(todo1.getIdOfUser().intValue()))).andExpect(jsonPath("$.toDo", is(actions)));

		this.mvc.perform(post("/api/todo/2/new").content(objMapper.writeValueAsString(todo2))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isCreated()).andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.date", is(LocalDateTime.of(2000, 5, 13, 1, 1, 1).toString())))
				.andExpect(jsonPath("$.idOfUser", is(todo2.getIdOfUser().intValue()))).andExpect(jsonPath("$.toDo", is(actions)));
	}
	
	@Test
	public void testPost_User() throws Exception {
		UserDTO userToSave1= new UserDTO(null, "nome1", "email1");
		UserDTO userToSave2 = new UserDTO(null, "nome2", "email2");
		this.mvc.perform(
				post("/api/users/1/new").content(objMapper.writeValueAsString(userToSave1))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.name", is("nome1"))).andExpect(jsonPath("$.email", is("email1")));
		
		this.mvc.perform(
				post("/api/users/2/new").content(objMapper.writeValueAsString(userToSave2))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.name", is("nome2"))).andExpect(jsonPath("$.email", is("email2")));
		
	}
	@Test
	public void testGetFindToDo() throws Exception {

		userService.setContext(1);
		User toSaveUser = new User(null, new ArrayList<>(), "db1", "db1");
		ToDo toSaveTodo = new ToDo(null, toSaveUser, new HashMap<>(), LocalDateTime.of(2005, 1, 1, 1, 1,1));
		toSaveTodo.addToDoAction("prova", false);
		toSaveUser.addToDo(toSaveTodo);
		User savedUser1 = userRepository.save(toSaveUser);
		ToDo savedTodo1 = todoRepository.save(toSaveTodo);
		this.mvc.perform(get("/api/todo/1/id/"+savedTodo1.getId().toString()).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(savedTodo1.getId().intValue())))
				.andExpect(jsonPath("$.date", is(savedTodo1.getLocalDateTime().toString())))
				.andExpect(jsonPath("$.idOfUser", is(savedTodo1.getIdOfUser().getId().intValue())))
				.andExpect(jsonPath("$.toDo", is(savedTodo1.getToDo())));
	}

}
