
package com.example.todoappmultidb.rest;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.anyLong;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static java.util.Arrays.asList;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.assertj.core.error.ErrorMessageFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todoappmultidb.model.User;
import com.example.todoappmultidb.rest.UserRestController;
import com.example.todoappmultidb.service.UserService;
import com.google.gson.Gson;

import javassist.NotFoundException;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserRestController.class)
public class UserRestControllerTest {

	@MockBean
	UserService userService;

	@Autowired
	private MockMvc mvc;

	@Test
	public void testAllUsersEmpty() throws Exception {
		when(userService.getAllUser()).thenReturn(Collections.emptyList());
		this.mvc.perform(get("/api/users").accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk()).andExpect(content().json("[]"));
	}

	@Test
	public void testAllUsers() throws Exception {
		when(userService.getAllUser())
				.thenReturn(asList(new User(1l, "nome1", "email1"), new User(2l, "nome2", "email2")));
		this.mvc.perform(get("/api/users").accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("nome1"))).andExpect(jsonPath("$[0].email", is("email1")))
				.andExpect(jsonPath("$[0].toDo", is(Collections.emptyList()))).andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].name", is("nome2"))).andExpect(jsonPath("$[1].email", is("email2")))
				.andExpect(jsonPath("$[1].toDo", is(Collections.emptyList())));
	}

	@Test
	public void testFindOneUserByIdWithNoExistingUser() throws Exception {
		when(userService.getUserById(anyLong())).thenReturn(null);
		this.mvc.perform(get("/api/users/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent())
				.andExpect(content().string(""));
	}

	@Test
	public void testFindOneUserByIdWithExistingUser() throws Exception {
		when(userService.getUserById(anyLong())).thenReturn(new User(1l, "nome1", "email1"));
		this.mvc.perform(get("/api/users/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.name", is("nome1")))
				.andExpect(jsonPath("$.email", is("email1")))
				.andExpect(jsonPath("$.toDo", is(Collections.emptyList())));
	}

	@Test
	public void testInsertNewUser() throws Exception {
		Gson json = new Gson();
		User u = new User(null, "nome1", "email1");
		when(userService.insertNewUser(u)).thenReturn(new User(1l, "nome1", "email1"));
		this.mvc.perform(post("/api/users/new").content(json.toJson(u)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("nome1"))).andExpect(jsonPath("$.email", is("email1")))
				.andExpect(jsonPath("$.toDo", is(Collections.emptyList())));
	}

	@Test
	public void testInsertNewUserNull() throws Exception {
		Gson json = new Gson();
		User u = new User(null, null, null);
		when(userService.insertNewUser(u)).thenThrow(new IllegalArgumentException("User with null property"));
		this.mvc.perform(post("/api/users/new").content(json.toJson(u)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isNoContent()).andExpect(status().reason("User with null property"));
	}
	@Test
	public void testUpdateUser() throws Exception{
		Gson json = new Gson();
		
		when(userService.updateUser(1,new User(null, "nome1", "email1"))).thenReturn(new User(1l, "nome1", "email1"));
		this.mvc.perform(put("/api/users/update/1").content(json.toJson(new User(null, "nome1", "email1"))).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("nome1"))).andExpect(jsonPath("$.email", is("email1")))
				.andExpect(jsonPath("$.toDo", is(Collections.emptyList())));
	}

	@Test
	public void testUpdateUserNullProperties() throws Exception {
		Gson json = new Gson();
		User u = new User(null, null, null);
		when(userService.updateUser(1,u)).thenThrow(new IllegalArgumentException("User with null property"));
		this.mvc.perform(put("/api/users/update/1").content(json.toJson(u)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isNoContent()).andExpect(status().reason("User with null property"));
	}

	@Test
	public void testUpdateUserNotExistingUser() throws Exception {
		Gson json = new Gson();
		User u = new User(null, "nome1", "email1");
		when(userService.updateUser(1,u)).thenThrow(new NotFoundException("Try to update not existing user"));
		this.mvc.perform(put("/api/users/update/1").content(json.toJson(u)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isNotFound()).andExpect(status().reason("Try to update not existing user"));
	}
}
