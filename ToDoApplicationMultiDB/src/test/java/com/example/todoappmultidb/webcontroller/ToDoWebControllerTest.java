package com.example.todoappmultidb.webcontroller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.todoappmultidb.dto.ToDoDTO;
import com.example.todoappmultidb.dto.UserDTO;
import com.example.todoappmultidb.model.ToDo;
import com.example.todoappmultidb.service.ToDoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javassist.NotFoundException;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ToDoWebController.class)
public class ToDoWebControllerTest {
	private final String ID = "id";
	private final String MESSAGE = "message";
	@Autowired
	private MockMvc mvc;
	@MockBean
	private ToDoService todoService;
	ObjectMapper objMapper = new ObjectMapper();

	@Test
	public void test_UserToDoView_ShowUserToDo() throws Exception {
		HashMap<String, Boolean> actions = new HashMap<>();
		actions.put("prova1", true);
		actions.put("prova2", false);
		List<ToDoDTO> todo = asList(new ToDoDTO(1l, 1l, actions, LocalDateTime.of(2005, 3, 4, 0, 0)),
				new ToDoDTO(2l, 1l, actions, LocalDateTime.of(2005, 3, 4, 0, 0)));
		when(todoService.findByUserId(new UserDTO(1l, null, null))).thenReturn(todo);
		mvc.perform(get("/todo/ofuser/1")).andExpect(view().name("todo")).andExpect(model().attribute("todo", todo))
				.andExpect(model().attribute(MESSAGE, "")).andExpect(model().attribute(ID, 1L));
	}

	@Test
	public void test_UserToDoView_EmptyUserToDo() throws Exception {
		when(todoService.findByUserId(new UserDTO(1l, null, null)))
				.thenThrow(new NotFoundException("Not found User ToDo  with id 1"));
		mvc.perform(get("/todo/ofuser/1")).andExpect(view().name("todo"))
				.andExpect(model().attribute(MESSAGE, "Not found User ToDo  with id 1"))
				.andExpect(model().attribute("todo", Collections.EMPTY_MAP)).andExpect(model().attribute(ID, 1L));
	}

	@Test
	public void test_EditToDo_IdFound() throws Exception {

		HashMap<String, Boolean> actions = new HashMap<>();
		actions.put("prova1", true);
		actions.put("prova2", false);
		ToDoDTO value = new ToDoDTO(1l, 1l, actions, LocalDateTime.of(2015, 5, 6, 0, 0));
		when(todoService.getToDoById(1l)).thenReturn(value);
		mvc.perform(get("/todo/edit/1")).andExpect(view().name("editToDo")).andExpect(model().attribute("todo", value))
				.andExpect(model().attribute(MESSAGE, ""));
	}

	@Test
	public void test_EditToDo_IdNotFound() throws Exception {
		when(todoService.getToDoById(1l)).thenThrow(new NotFoundException("Not found todo with id 1"));
		mvc.perform(get("/todo/edit/1")).andExpect(view().name("editToDo"))
				.andExpect(model().attribute(MESSAGE, "Not found todo with id 1"));

	}

	@Test
	public void test_EditNewToDo() throws Exception {
		mvc.perform(get("/todo/new/1")).andExpect(view().name("editToDo"))
				.andExpect(model().attribute("todo", new ToDoDTO(-1l, 1L, new HashMap<>(), null)))
				.andExpect(model().attribute(MESSAGE, ""));
		verifyNoInteractions(todoService);
	}

//post withoutID
	@Test
	public void test_PostToDoWithoutId_emptyActions() throws Exception {
		mvc.perform(post("/todo/save").param(ID, "-1").param("idOfUser", "1").param("date", "2005-01-12 00:00:00"))
				.andExpect(view().name("redirect:/todo/ofuser/1"));
		verify(todoService).saveToDo(new ToDoDTO(null, 1l, new HashMap<>(), LocalDateTime.of(2005, 1, 12, 0, 0)));
	}

	@Test
	public void test_PostToDoWithoutId_mapFull_emptyAdd() throws Exception {
		HashMap<String, Boolean> actions = new HashMap<>();

		actions.put("first", false);
		actions.put("second", true);

		mvc.perform(post("/todo/save").param(ID, "-1").param("idOfUser", "1")
				.param("actions", "{first=false,second=true}").param("date", "2005-01-12 00:00:00"))
				.andExpect(view().name("redirect:/todo/ofuser/1"));
		verify(todoService).saveToDo(new ToDoDTO(null, 1l, actions, LocalDateTime.of(2005, 1, 12, 0, 0)));
	}

	@Test
	public void test_PostToDoWithoutId_emptyMap_fullAdd() throws Exception {
		HashMap<String, Boolean> actions = new HashMap<>();
		actions.put("third", true);

		mvc.perform(post("/todo/save").param(ID, "-1").param("idOfUser", "1").param("date", "2005-01-12 00:00:00")
				.param("key", "third").param("value", "true")).andExpect(view().name("redirect:/todo/ofuser/1"));
		verify(todoService).saveToDo(new ToDoDTO(null, 1l, actions, LocalDateTime.of(2005, 1, 12, 0, 0)));
	}

	@Test
	public void test_PostToDoWithoutId_actionsFull() throws Exception {
		HashMap<String, Boolean> actions = new HashMap<>();

		actions.put("first", false);
		actions.put("second", true);
		actions.put("third", true);
		mvc.perform(
				post("/todo/save").param(ID, "-1").param("idOfUser", "1").param("actions", "{first=false,second=true}")
						.param("date", "2005-01-12 00:00:00").param("key", "third").param("value", "true"))
				.andExpect(view().name("redirect:/todo/ofuser/1"));
		verify(todoService).saveToDo(new ToDoDTO(null, 1l, actions, LocalDateTime.of(2005, 1, 12, 0, 0)));
	}

//post withID
	@Test
	public void test_PostToDoWithId_emptyActions() throws Exception {
		mvc.perform(post("/todo/save").param(ID, "1").param("idOfUser", "1").param("date", "2005-01-12 00:00:00")
				.param("actions", "").param("key", "").param("value", ""))
				.andExpect(view().name("redirect:/todo/ofuser/1"));
		verify(todoService).updateToDo(1l, new ToDoDTO(1l, 1l, new HashMap<>(), LocalDateTime.of(2005, 1, 12, 0, 0)));
	}

	@Test
	public void test_PostToDoWithId_mapFull_emptyAdd() throws Exception {
		HashMap<String, Boolean> actions = new HashMap<>();

		actions.put("first", false);
		actions.put("second", true);

		mvc.perform(post("/todo/save").param(ID, "1").param("idOfUser", "1")
				.param("actions", "{first=false,second=true}").param("date", "2005-01-12 00:00:00"))
				.andExpect(view().name("redirect:/todo/ofuser/1"));
		verify(todoService).updateToDo(1l, new ToDoDTO(1l, 1l, actions, LocalDateTime.of(2005, 1, 12, 0, 0)));
	}

	@Test
	public void test_PostToDoWithId_actionsFull() throws Exception {
		HashMap<String, Boolean> actions = new HashMap<>();

		actions.put("first", false);
		actions.put("second", true);
		actions.put("third", true);
		mvc.perform(
				post("/todo/save").param(ID, "1").param("idOfUser", "1").param("actions", "{first=false,second=true}")
						.param("date", "2005-01-12 00:00:00").param("key", "third").param("value", "true"))
				.andExpect(view().name("redirect:/todo/ofuser/1"));
		verify(todoService).updateToDo(1l, new ToDoDTO(1l, 1l, actions, LocalDateTime.of(2005, 1, 12, 0, 0)));
	}

	@Test
	public void test_PostToDoWithId_emptyMap_fullAdd() throws Exception {
		HashMap<String, Boolean> actions = new HashMap<>();

		actions.put("third", true);
		mvc.perform(post("/todo/save").param(ID, "1").param("idOfUser", "1").param("date", "2005-01-12 00:00:00")
				.param("key", "third").param("value", "true")).andExpect(view().name("redirect:/todo/ofuser/1"));
		verify(todoService).updateToDo(1l, new ToDoDTO(1l, 1l, actions, LocalDateTime.of(2005, 1, 12, 0, 0)));
	}

//add function
	@Test
	public void test_PostAddNewTask_emptyMap_fullTask() throws Exception {
		HashMap<String, Boolean> actions = new HashMap<>();
		actions.put("first", true);
		mvc.perform(post("/todo/addaction").param(ID, "1").param("idOfUser", "1").param("key", "first")
				.param("value", "true").param("date", "2005-01-12 00:00:00")).andExpect(view().name("editToDo"))
				.andExpect(model().attribute(MESSAGE, "")).andExpect(
						model().attribute("todo", new ToDoDTO(1l, 1l, actions, LocalDateTime.of(2005, 1, 12, 0, 0))));
	}

	@Test
	public void test_PostAddNewTask_fullMap_fullTask() throws Exception {
		HashMap<String, Boolean> actions = new HashMap<>();
		actions.put("first", false);
		actions.put("second", true);
		actions.put("third", true);
		mvc.perform(post("/todo/addaction").param(ID, "1").param("actions", "{first=false,second=true}")
				.param("idOfUser", "1").param("key", "third").param("value", "true")
				.param("date", "2005-01-12 00:00:00")).andExpect(view().name("editToDo"))
				.andExpect(model().attribute(MESSAGE, "")).andExpect(
						model().attribute("todo", new ToDoDTO(1l, 1l, actions, LocalDateTime.of(2005, 1, 12, 0, 0))));
	}

	@Test
	public void test_PostAddNewTask_emptyMap_emptyTask() throws Exception {
		HashMap<String, Boolean> actions = new HashMap<>();
		mvc.perform(post("/todo/addaction").param(ID, "1").param("idOfUser", "1").param("date", "2005-01-12 00:00:00"))
				.andExpect(view().name("editToDo")).andExpect(model().attribute(MESSAGE, "")).andExpect(
						model().attribute("todo", new ToDoDTO(1l, 1l, actions, LocalDateTime.of(2005, 1, 12, 0, 0))));
	}

	@Test
	public void test_PostAddNewTask_fullMap_emptyTask() throws Exception {
		HashMap<String, Boolean> actions = new HashMap<>();
		actions.put("first", false);
		actions.put("second", true);
		mvc.perform(post("/todo/addaction").param(ID, "1").param("actions", "{first=false,second=true}")
				.param("idOfUser", "1").param("date", "2005-01-12 00:00:00")).andExpect(view().name("editToDo"))
				.andExpect(model().attribute(MESSAGE, "")).andExpect(
						model().attribute("todo", new ToDoDTO(1l, 1l, actions, LocalDateTime.of(2005, 1, 12, 0, 0))));
	}

//error function
	@Test
	public void test_PostToDoWithId_notFoundExceptionByUpdate() throws Exception {
		doThrow(new NotFoundException("Not found any todo with id 1")).when(todoService).updateToDo(1l,
				new ToDoDTO(1l, 1l, new HashMap<>(), LocalDateTime.of(2005, 1, 12, 0, 0)));
		mvc.perform(post("/todo/save").param(ID, "1").param("idOfUser", "1").param("date", "2005-01-12 00:00:00"))
				.andExpect(view().name("redirect:/error/Not found any todo with id 1"));
	}

	@Test
	public void test_PostToDoWithoutId_notFoundExceptionBySave() throws Exception {
		doThrow(new NotFoundException("Not found any user with id 1")).when(todoService)
				.saveToDo(new ToDoDTO(null, 1l, new HashMap<>(), LocalDateTime.of(2005, 1, 12, 0, 0)));
		mvc.perform(post("/todo/save").param(ID, "-1").param("idOfUser", "1").param("date", "2005-01-12 00:00:00"))
				.andExpect(view().name("redirect:/error/Not found any user with id 1"));
	}

}
