package com.example.todoappmultidb.rest;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.core.IsNull;
import org.junit.Before;
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
import com.example.todoappmultidb.model.dto.UserDTO;
import com.example.todoappmultidb.service.ToDoService;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;

import javassist.NotFoundException;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ToDoRestController.class)
public class ToDoRestControllerTest {

	@MockBean
	ToDoService todoService;

	@Autowired
	private MockMvc mvc;
	private ObjectMapper objMapper;

	@Before
	public void setup() {
		objMapper = new ObjectMapper();
		objMapper.registerModule(new JavaTimeModule());
		objMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

	}

	@Test
	public void testGetAllToDoEmpty() throws Exception {
		when(todoService.findAll()).thenThrow(new NotFoundException("Not found any todo"));
		this.mvc.perform(get("/api/todo")).andExpect(status().isNoContent())
				.andExpect(status().reason("Not found any todo"));
	}

	@Test
	public void testGetAllToDo() throws Exception {
		HashMap<String, Boolean> actions = new HashMap<String, Boolean>();
		actions.put("first", false);
		actions.put("second",true);
		when(todoService.findAll()).thenReturn(
				asList(new ToDoDTO(1l, 1l, new HashMap<String, Boolean>(), LocalDateTime.of(2000, 5, 13, 1, 1, 1)),
						new ToDoDTO(2l, 2l, actions, LocalDateTime.of(2001, 6, 3, 5, 0, 8))));
		this.mvc.perform(get("/api/todo").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isOk()).andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].date", is(LocalDateTime.of(2000, 5, 13, 1, 1, 1).toString())))
				.andExpect(jsonPath("$[0].idOfUser", is(1)))
				.andExpect(jsonPath("$[0].toDo", is(new HashMap<String, Boolean>())))
				.andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].date", is(LocalDateTime.of(2001, 6, 3, 5, 0, 8).toString())))
				.andExpect(jsonPath("$[1].idOfUser", is(2)))
				.andExpect(jsonPath("$[1].toDo", is(actions)));
	}

	@Test
	public void testGetOneToDoById() throws Exception {
		when(todoService.findByIdDTO(1l)).thenReturn(
				new ToDoDTO(1l, 1l, new HashMap<String, Boolean>(), LocalDateTime.of(2000, 5, 13, 1, 1, 1)));
		this.mvc.perform(get("/api/todo/1").contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.date", is(LocalDateTime.of(2000, 5, 13, 1, 1, 1).toString())))
				.andExpect(jsonPath("$.idOfUser", is(1)))
				.andExpect(jsonPath("$.toDo", is(new HashMap<String, Boolean>())));
	}

	@Test
	public void testGetOneToDoByIdNotFound() throws Exception {
		when(todoService.findByIdDTO(2l)).thenThrow(new NotFoundException("Not found any todo with id 2"));
		this.mvc.perform(get("/api/todo/2").contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isNoContent())
				.andExpect(status().reason("Not found any todo with id 2"));
	}

	/*not developed into service class
	 * @Test public void testGetToDoByUserId() throws Exception {
	 * when(todoService.findByUserId(new UserDTO(1L, null, null))).thenReturn(
	 * asList(new ToDoDTO(1l, 1l, new HashMap<String, Boolean>(),
	 * LocalDateTime.of(2000, 5, 13, 1, 1, 1)), new ToDoDTO(2l, 2l, new
	 * HashMap<String, Boolean>(), LocalDateTime.of(2001, 6, 3, 5, 0, 8))));
	 * 
	 * this.mvc.perform(get("/api/todo/ofuser/1").contentType(MediaType.
	 * APPLICATION_JSON)).andExpect(status().isOk()) .andExpect(jsonPath("$[0].id",
	 * is(1))) .andExpect(jsonPath("$[0].date", is(LocalDateTime.of(2000, 5, 13, 1,
	 * 1, 1).toString()))) .andExpect(jsonPath("$[0].idOfUser", is(1)))
	 * .andExpect(jsonPath("$[0].toDo", is(new HashMap<String, Boolean>())))
	 * .andExpect(jsonPath("$[1].id", is(2))) .andExpect(jsonPath("$[1].date",
	 * is(LocalDateTime.of(2001, 6, 3, 5, 0, 8).toString())))
	 * .andExpect(jsonPath("$[1].idOfUser", is(2))) .andExpect(jsonPath("$[1].toDo",
	 * is(new HashMap<String, Boolean>()))); }
	 * 
	 * @Test public void testGetToDoByUserId_foundAnything() throws Exception {
	 * when(todoService.findByUserId(new UserDTO(1l, null, null))) .thenThrow(new
	 * NotFoundException("Not found todo with user id 1"));
	 * this.mvc.perform(get("/api/todo/ofuser/1").contentType(MediaType.
	 * APPLICATION_JSON)) .andExpect(status().isNotFound()).andExpect(status().
	 * reason("Not found todo with user id 1")); }
	 */

	@Test
	public void testPostNewToDo_emptyMap() throws Exception {

		ToDoDTO todo = new ToDoDTO(null, 1l, new HashMap<String, Boolean>(), LocalDateTime.of(2000, 5, 13, 1, 1, 1));
		when(todoService.save(todo)).thenReturn(
				new ToDoDTO(1l, 1l, new HashMap<String, Boolean>(), LocalDateTime.of(2000, 5, 13, 1, 1, 1)));
		this.mvc.perform(post("/api/todo/new").content(objMapper.writeValueAsString(todo))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.date", is(LocalDateTime.of(2000, 5, 13, 1, 1, 1).toString())))
				.andExpect(jsonPath("$.idOfUser", is(1)))
				.andExpect(jsonPath("$.toDo", is(new HashMap<String, Boolean>())));
	}

	@Test
	public void testPostNewToDo_FullMap() throws Exception {

		HashMap<String, Boolean> actions = new HashMap<String, Boolean>();
		actions.put("first", false);
		actions.put("second", true);
		ToDoDTO todo = new ToDoDTO(null, 1l, actions, LocalDateTime.of(2000, 5, 13, 1, 1, 1));
		when(todoService.save(todo))
				.thenReturn(new ToDoDTO(1l, 1l, actions, LocalDateTime.of(2000, 5, 13, 1, 1, 1)));
		this.mvc.perform(post("/api/todo/new").content(objMapper.writeValueAsString(todo))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isCreated()).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.date", is(LocalDateTime.of(2000, 5, 13, 1, 1, 1).toString())))
				.andExpect(jsonPath("$.idOfUser", is(1))).andExpect(jsonPath("$.toDo", is(actions)));
	}

	@Test
	public void testPostNewToDoNullValue() throws Exception {
		ToDoDTO todo = new ToDoDTO(null, null, null, null);
		when(todoService.save(todo)).thenThrow(new IllegalArgumentException("Insert ToDo with null value"));
		this.mvc.perform(post("/api/todo/new").content(objMapper.writeValueAsString(todo))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict()).andExpect(status().reason("Insert ToDo with null value"));
	}

	@Test
	public void testPutUpdateToDo_emptyMaps() throws Exception {
		ToDoDTO todo = new ToDoDTO(null, 1l, new HashMap<String, Boolean>(), LocalDateTime.of(2000, 5, 13, 1, 1, 1));
		when(todoService.updateById(1l, todo)).thenReturn(
				new ToDoDTO(1l, 1l, new HashMap<String, Boolean>(), LocalDateTime.of(2000, 5, 13, 1, 1, 1)));
		this.mvc.perform(put("/api/todo/update/1").content(objMapper.writeValueAsString(todo))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.date", is(LocalDateTime.of(2000, 5, 13, 1, 1, 1).toString())))
				.andExpect(jsonPath("$.idOfUser", is(1)))
				.andExpect(jsonPath("$.toDo", is(new HashMap<String, Boolean>())));
	}
	@Test
	public void testPutUpdateToDo_populatedMaps() throws Exception {
		HashMap<String, Boolean> actions = new HashMap<String, Boolean>();
		actions.put("first", false);
		actions.put("second", true);
		ToDoDTO todo = new ToDoDTO(null, 1l, actions, LocalDateTime.of(2000, 5, 13, 1, 1, 1));
		when(todoService.updateById(1l, todo)).thenReturn(
				new ToDoDTO(1l, 1l, actions, LocalDateTime.of(2000, 5, 13, 1, 1, 1)));
		this.mvc.perform(put("/api/todo/update/1").content(objMapper.writeValueAsString(todo))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.date", is(LocalDateTime.of(2000, 5, 13, 1, 1, 1).toString())))
				.andExpect(jsonPath("$.idOfUser", is(1)))
				.andExpect(jsonPath("$.toDo", is(actions)));
	}
	@Test
	public void testPutUpdateToDoWithNullValue() throws Exception {
		ToDoDTO todo = new ToDoDTO(null, null, null, null);
		when(todoService.updateById(1l, todo)).thenThrow(new IllegalArgumentException("Update ToDo with null value"));
		this.mvc.perform(put("/api/todo/update/1").content(objMapper.writeValueAsString(todo))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict()).andExpect(status().reason("Update ToDo with null value"));
	}

	@Test
	public void testPutUpdateToDoNotFound() throws Exception {
		ToDoDTO todo = new ToDoDTO(null, 1l, new HashMap<String, Boolean>(), LocalDateTime.of(2000, 5, 13, 1, 1, 1));
		when(todoService.updateById(1l, todo)).thenThrow(new NotFoundException("Not Found any ToDo with id 1"));
		this.mvc.perform(put("/api/todo/update/1").content(objMapper.writeValueAsString(todo))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andExpect(status().reason("Not Found any ToDo with id 1"));
	}

}
