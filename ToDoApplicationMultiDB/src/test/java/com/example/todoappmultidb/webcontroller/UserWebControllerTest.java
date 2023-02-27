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

	@Test
	public void test_HomeView_ShowsUsers() throws Exception {
		List<UserDTO> users = asList(new UserDTO(1L, "test_1", "test_1"), new UserDTO(2L, "test_2", "test_2"));
		when(userService.getAllUser()).thenReturn(users);
		mvc.perform(get("/")).andExpect(view().name("index")).andExpect(model().attribute("users", users))
				.andExpect(model().attribute(MESSAGE, ""));

	}

	@Test
	public void test_HomeView_ShowsMessageWhenThereAreNoUsers() throws Exception {
		when(userService.getAllUser()).thenReturn(Collections.emptyList());
		mvc.perform(get("/")).andExpect(view().name("index"))
				.andExpect(model().attribute("users", Collections.emptyList()))
				.andExpect(model().attribute(MESSAGE, "No user"));
	}

	@Test
	public void test_HomeView_interaction_dbDefault() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService);
		mvc.perform(get("/"));
		inOrder.verify(userService).setContext(1);
		inOrder.verify(userService).getAllUser();
	}

	@Test
	public void test_HomeView_interaction_db2() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService);
		mvc.perform(get("/").param("db", "2"));
		inOrder.verify(userService).setContext(2);
		inOrder.verify(userService).getAllUser();
	}

	@Test
	public void test_EditUser_WhenUserIsFound() throws Exception {
		UserDTO user = new UserDTO(1L, "test", "test");
		when(userService.getUserById(1L)).thenReturn(user);
		mvc.perform(get("/user/edit/1")).andExpect(view().name("editUser")).andExpect(model().attribute("user", user))
				.andExpect(model().attribute(MESSAGE, ""));
	}

	@Test
	public void test_EditUser_WhenUserIsNotFound() throws Exception {
		when(userService.getUserById(1L)).thenReturn(null);
		mvc.perform(get("/user/edit/1")).andExpect(view().name("editUser"))
				.andExpect(model().attribute("user", nullValue()))
				.andExpect(model().attribute(MESSAGE, "No user found with id: 1"));
	}

	@Test
	public void test_EditUser_interaction_dbDefault() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService);
		mvc.perform(get("/user/edit/1"));
		inOrder.verify(userService).setContext(1);
		inOrder.verify(userService).getUserById(1l);
	}

	@Test
	public void test_EditUser_interaction_db2() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService);
		mvc.perform(get("/user/edit/1").param("db", "2"));
		inOrder.verify(userService).setContext(2);
		inOrder.verify(userService).getUserById(1l);
	}

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

	@Test
	public void test_PostUserWithoutId_ShouldInsertNewUser() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService);
		mvc.perform(post("/user/save").param("name", "test name").param("email", "test email"))
				.andExpect(view().name("redirect:/"));
		inOrder.verify(userService).setContext(1);
		inOrder.verify(userService).insertNewUser(new UserDTO(null, "test name", "test email"));

	}

	@Test
	public void test_Save_db2_insert() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService);
		mvc.perform(post("/user/save").param("name", "test name").param("email", "test email").param("db", "2"))
				.andExpect(view().name("redirect:/?db=2"));
		inOrder.verify(userService).setContext(2);
		inOrder.verify(userService).insertNewUser(new UserDTO(null, "test name", "test email"));
	}

	@Test
	public void test_Save_db1_insert() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService);
		mvc.perform(post("/user/save").param("name", "test name").param("email", "test email").param("db", "1"))
				.andExpect(view().name("redirect:/?db=1"));
		inOrder.verify(userService).setContext(1);
		inOrder.verify(userService).insertNewUser(new UserDTO(null, "test name", "test email"));
	}

	@Test
	public void test_PostUserWithId_ShouldUpdateExistingUser() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService);
		mvc.perform(post("/user/save").param("id", "1").param("name", "test name").param("email", "test email"))
				.andExpect(view().name("redirect:/"));
		inOrder.verify(userService).setContext(1);
		inOrder.verify(userService).updateUserById(1L, new UserDTO(1L, "test name", "test email"));

	}

	@Test
	public void test_Save_db1_update() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService);
		mvc.perform(post("/user/save").param("id", "1").param("name", "test name").param("email", "test email")
				.param("db", "1")).andExpect(view().name("redirect:/?db=1"));
		inOrder.verify(userService).setContext(1);
		inOrder.verify(userService).updateUserById(1L, new UserDTO(1L, "test name", "test email"));

	}

	@Test
	public void test_Save_db2_update() throws Exception {
		InOrder inOrder = Mockito.inOrder(userService);
		mvc.perform(post("/user/save").param("id", "1").param("name", "test name").param("email", "test email")
				.param("db", "2")).andExpect(view().name("redirect:/?db=2"));
		inOrder.verify(userService).setContext(2);
		inOrder.verify(userService).updateUserById(1L, new UserDTO(1L, "test name", "test email"));

	}
}