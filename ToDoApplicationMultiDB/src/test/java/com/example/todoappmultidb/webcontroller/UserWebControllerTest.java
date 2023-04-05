package com.example.todoappmultidb.webcontroller;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.todoappmultidb.model.dto.UserDTO;
import com.example.todoappmultidb.service.UserService;

import javassist.NotFoundException;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserWebController.class)
public class UserWebControllerTest {
	private final String MESSAGE = "message";
	@Autowired
	private MockMvc mvc;
	@MockBean
	private UserService userService;

	@Test
	public void testStatus200() throws Exception {
		mvc.perform(get("/")).andExpect(status().is2xxSuccessful());
	}

//index
	@Test
	public void test_HomeView_ShowsUsers() throws Exception {
		List<UserDTO> users = asList(new UserDTO(1L, "test_1", "test1@email.com"),
				new UserDTO(2L, "test_2", "test2@email.com"));
		when(userService.getAllUser()).thenReturn(users);
		mvc.perform(get("/")).andExpect(view().name("index")).andExpect(model().attribute("users", users))
				.andExpect(model().attribute(MESSAGE, ""));

	}

	@Test
	public void test_HomeView_ShowsMessageWhenThereAreNoUsers() throws Exception {
		when(userService.getAllUser()).thenThrow(new NotFoundException("No user"));
		mvc.perform(get("/")).andExpect(view().name("index"))
				.andExpect(model().attribute("users", Collections.emptyList()))
				.andExpect(model().attribute(MESSAGE, "No user"));
	}

	@Test
	public void test_HomeView_interaction_dbDefault() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService);
		mvc.perform(get("/"));
		inOrder.verify(userService).setDatabase(1);
		inOrder.verify(userService).getAllUser();
	}

	@Test
	public void test_HomeView_interaction_db2() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService);
		mvc.perform(get("/").param("db", "2"));
		inOrder.verify(userService).setDatabase(2);
		inOrder.verify(userService).getAllUser();
	}

//edit
	@Test
	public void test_EditUser_WhenUserIsFound() throws Exception {
		UserDTO user = new UserDTO(1L, "test", "test@email.com");
		when(userService.getUserById(1L)).thenReturn(user);
		mvc.perform(get("/user/edit/1")).andExpect(view().name("editUser")).andExpect(model().attribute("user", user))
				.andExpect(model().attribute(MESSAGE, ""));
	}

	@Test
	public void test_EditUser_WhenUserIsNotFound() throws Exception {
		when(userService.getUserById(1L)).thenThrow(new NotFoundException("No user found with id: 1"));
		mvc.perform(get("/user/edit/1")).andExpect(view().name("editUser"))
				.andExpect(model().attribute("user", nullValue()))
				.andExpect(model().attribute(MESSAGE, "No user found with id: 1"));
	}

	@Test
	public void test_EditUser_interaction_dbDefault() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService);
		when(userService.getUserById(1l)).thenReturn(new UserDTO(1L, "prova", "test@email.com"));
		mvc.perform(get("/user/edit/1"));
		inOrder.verify(userService).setDatabase(1);
		inOrder.verify(userService).getUserById(1l);
	}

	@Test
	public void test_EditUser_interaction_db2() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService);
		when(userService.getUserById(1l)).thenReturn(new UserDTO(1L, "prova", "test@email.com"));
		mvc.perform(get("/user/edit/1").param("db", "2"));
		inOrder.verify(userService).setDatabase(2);
		inOrder.verify(userService).getUserById(1l);
	}

//new
	@Test
	public void test_EditNewUser() throws Exception {
		mvc.perform(get("/user/new")).andExpect(view().name("editUser"))
				.andExpect(model().attribute("user", new UserDTO())).andExpect(model().attribute(MESSAGE, ""));
		verifyNoInteractions(userService);
	}

	@Test
	public void test_EditNewUser_forwardDB2Parameter() throws Exception {
		mvc.perform(get("/user/new").param("db", "2")).andExpect(view().name("editUser"))
				.andExpect(model().attribute("user", new UserDTO())).andExpect(model().attribute(MESSAGE, ""));
		verifyNoInteractions(userService);
	}

//save
	@Test
	public void test_PostUserWithoutId_fullValues() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService);
		mvc.perform(post("/user/save").param("name", "test name").param("email", "test@email.com"))
				.andExpect(view().name("redirect:/"));
		inOrder.verify(userService).setDatabase(1);
		inOrder.verify(userService).insertNewUser(new UserDTO(null, "test name", "test@email.com"));

	}

	@Test
	public void test_PostUserWithoutId_emptyValues() throws Exception {
		when(userService.insertNewUser(new UserDTO(null, "", ""))).thenThrow(new IllegalArgumentException("bad_value"));
		mvc.perform(post("/user/save").param("name", "").param("email", "")).andExpect(status().is(302))
				.andExpect(view().name("redirect:/error/bad_value"));
	}

	@Test
	public void test_PostUserWithoutId_emptyName() throws Exception {
		when(userService.insertNewUser(new UserDTO(null, "", "test@email.com")))
				.thenThrow(new IllegalArgumentException("bad_value"));
		mvc.perform(post("/user/save").param("name", "").param("email", "test@email.com")).andExpect(status().is(302))
				.andExpect(view().name("redirect:/error/bad_value"));
	}

	@Test
	public void test_PostUserWithoutId_emptyEmail() throws Exception {
		when(userService.insertNewUser(new UserDTO(null, "test name", "")))
				.thenThrow(new IllegalArgumentException("bad_value"));
		mvc.perform(post("/user/save").param("name", "test name").param("email", "")).andExpect(status().is(302))
				.andExpect(view().name("redirect:/error/bad_value"));
	}

	@Test
	public void test_Save_db2_insert() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService);
		mvc.perform(post("/user/save").param("name", "test name").param("email", "test@email.com").param("db", "2"))
				.andExpect(view().name("redirect:/?db=2"));
		inOrder.verify(userService).setDatabase(2);
		inOrder.verify(userService).insertNewUser(new UserDTO(null, "test name", "test@email.com"));
	}

	@Test
	public void test_Save_db1_insert() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService);
		mvc.perform(post("/user/save").param("name", "test name").param("email", "test@email.com").param("db", "1"))
				.andExpect(view().name("redirect:/?db=1"));
		inOrder.verify(userService).setDatabase(1);
		inOrder.verify(userService).insertNewUser(new UserDTO(null, "test name", "test@email.com"));
	}

	@Test
	public void test_PostUserWithId_ShouldUpdateExistingUser() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService);
		mvc.perform(post("/user/save").param("id", "1").param("name", "test name").param("email", "test@email.com"))
				.andExpect(view().name("redirect:/"));
		inOrder.verify(userService).setDatabase(1);
		inOrder.verify(userService).updateUserById(1L, new UserDTO(1L, "test name", "test@email.com"));

	}

	@Test
	public void test_Save_db1_update() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService);
		mvc.perform(post("/user/save").param("id", "1").param("name", "test name").param("email", "test@email.com")
				.param("db", "1")).andExpect(view().name("redirect:/?db=1"));
		inOrder.verify(userService).setDatabase(1);
		inOrder.verify(userService).updateUserById(1L, new UserDTO(1L, "test name", "test@email.com"));

	}

	@Test
	public void test_Save_db2_update() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService);
		mvc.perform(post("/user/save").param("id", "1").param("name", "test name").param("email", "test@email.com")
				.param("db", "2")).andExpect(view().name("redirect:/?db=2"));
		inOrder.verify(userService).setDatabase(2);
		inOrder.verify(userService).updateUserById(1L, new UserDTO(1L, "test name", "test@email.com"));

	}
}
