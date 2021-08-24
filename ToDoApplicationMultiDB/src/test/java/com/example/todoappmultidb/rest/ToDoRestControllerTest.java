package com.example.todoappmultidb.rest;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.example.todoappmultidb.model.dto.ToDoDTO;
import com.example.todoappmultidb.service.ToDoService;
import com.google.gson.Gson;

import javassist.NotFoundException;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ToDoRestController.class)
public class ToDoRestControllerTest {

	@MockBean
	ToDoService todoService;

	@Autowired
	private MockMvc mvc;

	@Test
	public void testGetAllToDoEmpty() throws Exception {
		when(todoService.getAllToDo()).thenThrow(new NotFoundException("Not found any todo"));
		this.mvc.perform(get("/api/todo")).andExpect(status().isNoContent())
				.andExpect(status().reason("Not found any todo"));
	}

	@Test
	public void testGetAllToDo() throws Exception {
		when(todoService.getAllToDo()).thenReturn(
				asList(new ToDoDTO(1l, 1l, new HashMap<String, Boolean>(), LocalDateTime.of(2000, 5, 13, 1, 1, 1)),
						new ToDoDTO(2l, 2l, new HashMap<String, Boolean>(), LocalDateTime.of(2001, 6, 3, 5, 0, 8))));
		this.mvc.perform(get("/api/todo").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isOk()).andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].date", is(LocalDateTime.of(2000, 5, 13, 1, 1, 1).toString())))
				.andExpect(jsonPath("$[0].idOfUser", is(1))).andExpect(jsonPath("$[0].toDo", is(new HashMap<String, Boolean>())))
				.andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].date", is(LocalDateTime.of(2001, 6, 3, 5, 0, 8).toString())))
				.andExpect(jsonPath("$[1].idOfUser", is(2))).andExpect(jsonPath("$[1].toDo", is(new HashMap<String, Boolean>())));
	}

	@Test
	public void testGetOneToDoById() throws Exception {
		when(todoService.getToDoById(1l)).thenReturn(
				new ToDoDTO(1l, 1l, new HashMap<String, Boolean>(), LocalDateTime.of(2000, 5, 13, 1, 1, 1)));
		this.mvc.perform(get("/api/todo/1").contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.date", is(LocalDateTime.of(2000, 5, 13, 1, 1, 1).toString())))
				.andExpect(jsonPath("$.idOfUser", is(1))).andExpect(jsonPath("$.toDo", is(new HashMap<String, Boolean>())));
	}

	@Test
	public void testGetOneToDoByIdNotFound() throws Exception {
		when(todoService.getToDoById(2l)).thenThrow(new NotFoundException("Not found any todo with id 2"));
		this.mvc.perform(get("/api/todo/2").contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isNoContent())
				.andExpect(status().reason("Not found any todo with id 2"));
	}

	@Test
	public void testPostNewToDo() throws Exception {
		Gson json = new Gson();
		ToDoDTO td = new ToDoDTO(null, 1l, new HashMap<String, Boolean>(), LocalDateTime.of(2000, 5, 13, 1, 1, 1));
		String toSend=json.toJson(td);
		this.mvc.perform(post("/api/todo/new").content(toSend).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.date", is(LocalDateTime.of(2000, 5, 13, 1, 1, 1).toString())))
				.andExpect(jsonPath("$.idOfUser", is(1))).andExpect(jsonPath("$.toDo", is(new HashMap())));
	}

	@Test
	public void testPostNewToDoNullValue() throws Exception {
		Gson json = new Gson();
		ToDoDTO td = new ToDoDTO(null, null, null, null);
		this.mvc.perform(post("/api/todo/new").content(json.toJson(td)).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isConflict());
	}
}
