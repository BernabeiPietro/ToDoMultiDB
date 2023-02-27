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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.todoappmultidb.model.dto.ToDoDTO;
import com.example.todoappmultidb.service.ToDoService;
import com.example.todoappmultidb.service.UserService;

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
	@MockBean
	private UserService userService;

	private ToDoDTO notImportant;

	@Before
	public void setup() {
		notImportant = new ToDoDTO(1l, 1l, new HashMap<String, Boolean>(), LocalDateTime.of(2005, 3, 4, 0, 0));
	}

	@Test
	public void test_UserToDoView_ShowUserToDo() throws Exception {
		HashMap<String, Boolean> actions = new HashMap<>();
		actions.put("prova1", true);
		actions.put("prova2", false);
		List<ToDoDTO> todo = asList(new ToDoDTO(1l, 1l, actions, LocalDateTime.of(2005, 3, 4, 0, 0)),
				new ToDoDTO(2l, 1l, actions, LocalDateTime.of(2005, 3, 4, 0, 0)));
		when(todoService.findByUserId(1l)).thenReturn(todo);
		mvc.perform(get("/todo/ofuser/1")).andExpect(view().name("todo")).andExpect(model().attribute("todo", todo))
				.andExpect(model().attribute(MESSAGE, "")).andExpect(model().attribute(ID, 1L));
	}

	@Test
	public void test_UserToDoView_EmptyUserToDo() throws Exception {
		when(todoService.findByUserId(1l)).thenThrow(new NotFoundException("Not found User ToDo  with id 1"));
		mvc.perform(get("/todo/ofuser/1")).andExpect(view().name("todo"))
				.andExpect(model().attribute(MESSAGE, "Not found User ToDo  with id 1"))
				.andExpect(model().attribute("todo", Collections.EMPTY_MAP)).andExpect(model().attribute(ID, 1L));
	}

//show_db

	@Test
	public void test_UserToDoView_db_2() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService, todoService);
		List<ToDoDTO> todo = asList(notImportant);
		when(todoService.findByUserId(1l)).thenReturn(todo);

		mvc.perform(get("/todo/ofuser/1").param("db", "2"));
		inOrder.verify(userService).setContext(2);
		inOrder.verify(todoService).findByUserId(1l);
	}

	@Test
	public void test_userToDoView_db_default() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService, todoService);
		List<ToDoDTO> todo = asList(notImportant);
		when(todoService.findByUserId(1l)).thenReturn(todo);

		mvc.perform(get("/todo/ofuser/1"));
		inOrder.verify(userService).setContext(1);
		inOrder.verify(todoService).findByUserId(1l);
	}

//edit
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

//edit_db
	@Test
	public void test_EditToDo_db_2() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService, todoService);
		when(todoService.getToDoById(1)).thenReturn(notImportant);
		mvc.perform(get("/todo/edit/1").param("db", "2"));
		inOrder.verify(userService).setContext(2);
		inOrder.verify(todoService).getToDoById(1l);
	}

	@Test
	public void test_EditToDo_db_default() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService, todoService);
		when(todoService.getToDoById(1)).thenReturn(notImportant);
		mvc.perform(get("/todo/edit/1"));
		inOrder.verify(userService).setContext(1);
		inOrder.verify(todoService).getToDoById(1l);
	}

//new
	@Test
	public void test_EditNewToDo_db_default() throws Exception {
		mvc.perform(get("/todo/new/1")).andExpect(view().name("editToDo"))
				.andExpect(model().attribute("todo", new ToDoDTO(-1l, 1L, new HashMap<>(), null)))
				.andExpect(model().attribute(MESSAGE, ""));
		verifyNoInteractions(todoService);
	}

	@Test
	public void test_EditNewToDo_db_2() throws Exception {
		mvc.perform(get("/todo/new/1").param("db", "2")).andExpect(view().name("editToDo"))
				.andExpect(model().attribute("todo", new ToDoDTO(-1l, 1L, new HashMap<>(), null)))
				.andExpect(model().attribute(MESSAGE, ""));
		verifyNoInteractions(todoService);
	}

//save new
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

//save new_db
	@Test
	public void test_saveTodo_db_default() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService, todoService);
		mvc.perform(post("/todo/save").param(ID, "-1").param("idOfUser", "1").param("date", "2005-01-12 00:00:00"))
				.andExpect(view().name("redirect:/todo/ofuser/1"));
		inOrder.verify(userService).setContext(1);
		inOrder.verify(todoService)
				.saveToDo(new ToDoDTO(null, 1l, new HashMap<>(), LocalDateTime.of(2005, 1, 12, 0, 0)));
	}

	@Test
	public void test_saveTodo_db_1() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService, todoService);
		mvc.perform(post("/todo/save").param(ID, "-1").param("idOfUser", "1").param("date", "2005-01-12 00:00:00")
				.param("db", "1")).andExpect(view().name("redirect:/todo/ofuser/1?db=1"));
		inOrder.verify(userService).setContext(1);
		inOrder.verify(todoService)
				.saveToDo(new ToDoDTO(null, 1l, new HashMap<>(), LocalDateTime.of(2005, 1, 12, 0, 0)));
	}

	@Test
	public void test_saveTodo_db_2() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService, todoService);
		mvc.perform(post("/todo/save").param(ID, "-1").param("idOfUser", "1").param("date", "2005-01-12 00:00:00")
				.param("db", "2")).andExpect(view().name("redirect:/todo/ofuser/1?db=2"));
		inOrder.verify(userService).setContext(2);
		inOrder.verify(todoService)
				.saveToDo(new ToDoDTO(null, 1l, new HashMap<>(), LocalDateTime.of(2005, 1, 12, 0, 0)));
	}

//update
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

//update_db
	@Test
	public void test_UpdateToDo_db_default() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService, todoService);
		mvc.perform(post("/todo/save").param(ID, "1").param("idOfUser", "1").param("date", "2005-01-12 00:00:00")
				.param("actions", "").param("key", "").param("value", ""))
				.andExpect(view().name("redirect:/todo/ofuser/1"));
		inOrder.verify(userService).setContext(1);
		inOrder.verify(todoService).updateToDo(1l,
				new ToDoDTO(1l, 1l, new HashMap<>(), LocalDateTime.of(2005, 1, 12, 0, 0)));

	}

	@Test
	public void test_UpdateToDo_db_1() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService, todoService);
		mvc.perform(post("/todo/save").param(ID, "1").param("idOfUser", "1").param("date", "2005-01-12 00:00:00")
				.param("actions", "").param("key", "").param("value", "").param("db", "1"))
				.andExpect(view().name("redirect:/todo/ofuser/1?db=1"));
		inOrder.verify(userService).setContext(1);
		inOrder.verify(todoService).updateToDo(1l,
				new ToDoDTO(1l, 1l, new HashMap<>(), LocalDateTime.of(2005, 1, 12, 0, 0)));

	}

	@Test
	public void test_UpdateToDo_db_2() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService, todoService);
		mvc.perform(post("/todo/save").param(ID, "1").param("idOfUser", "1").param("date", "2005-01-12 00:00:00")
				.param("actions", "").param("key", "").param("value", "").param("db", "2"))
				.andExpect(view().name("redirect:/todo/ofuser/1?db=2"));
		inOrder.verify(userService).setContext(2);
		inOrder.verify(todoService).updateToDo(1l,
				new ToDoDTO(1l, 1l, new HashMap<>(), LocalDateTime.of(2005, 1, 12, 0, 0)));
	}

//add task
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
		mvc.perform(post("/todo/addaction").param(ID, "1").param("idOfUser", "1")
				.param("actions", "{first=false,second=true}").param("date", "2005-01-12 00:00:00"))
				.andExpect(view().name("editToDo")).andExpect(model().attribute(MESSAGE, "")).andExpect(
						model().attribute("todo", new ToDoDTO(1l, 1l, actions, LocalDateTime.of(2005, 1, 12, 0, 0))));
	}

	@Test
	public void test_PostAddNewTask_db2() throws Exception {
		HashMap<String, Boolean> actions = new HashMap<>();
		actions.put("first", true);
		mvc.perform(post("/todo/addaction?db=2").param(ID, "1").param("idOfUser", "1").param("key", "first")
				.param("value", "true").param("date", "2005-01-12 00:00:00")).andExpect(view().name("editToDo"))
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
