package com.example.todoappmultidb.webcontroller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.todoappmultidb.dto.ToDoDTO;
import com.example.todoappmultidb.dto.UserDTO;
import com.example.todoappmultidb.service.ToDoService;

import javassist.NotFoundException;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ToDoWebController.class)
public class ToDoWebControllerTest {
	private final String MESSAGE = "message";
	@Autowired
	private MockMvc mvc;
	@MockBean
	private ToDoService todoService;

	@Test
	public void test_UserToDoView_ShowUserToDo() throws Exception {
		List<ToDoDTO> todo = asList(new ToDoDTO(1l, 1l, new HashMap<>(), LocalDateTime.of(2005, 3, 4, 0, 0)));
		when(todoService.findByUserId(new UserDTO(1l, null, null))).thenReturn(todo);
		mvc.perform(get("/todo/ofuser/1")).andExpect(view().name("todo")).andExpect(model().attribute("todo", todo))
				.andExpect(model().attribute(MESSAGE, ""));
	}

	@Test
	public void test_UserToDoView_EmptyUserToDo() throws Exception {
		when(todoService.findByUserId(new UserDTO(1l, null, null)))
				.thenThrow(new NotFoundException("Not found User ToDo  with id 1"));
		mvc.perform(get("/todo/ofuser/1")).andExpect(view().name("todo"))
				.andExpect(model().attribute(MESSAGE, "Not found User ToDo  with id 1"));
	}

	@Test
	public void test_EditToDo_IdFound() throws Exception {

		ToDoDTO value = new ToDoDTO(1l, 1l, new HashMap<>(), LocalDateTime.of(2015, 5, 6, 0, 0));
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
		mvc.perform(get("/todo/new")).andExpect(view().name("editToDo"))
				.andExpect(model().attribute("todo", new ToDoDTO())).andExpect(model().attribute(MESSAGE, ""));
		verifyNoInteractions(todoService);
	}

	@Test
	public void test_PostToDoWithId() throws Exception {
		mvc.perform(post("/todo/save").param("id", "1").param("user_id", "1").param("todo", "").param("date",
				"2005-01-12T00:00")).andExpect(view().name("redirect:/"));
		verify(todoService).updateToDo(1l, new ToDoDTO(1l, 1l, new HashMap<>(), LocalDateTime.of(2005, 1, 12, 0, 0)));
	}
}
