
package com.example.todoappmultidb.rest;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.example.todoappmultidb.model.dto.UserDTO;
import com.example.todoappmultidb.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javassist.NotFoundException;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserRestController.class)
public class UserRestControllerTest {

	@MockBean
	UserService userService;

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
	public void testGetAllUsersEmpty_db1() throws Exception {
		InOrder inOrder= Mockito.inOrder(userService);
		when(userService.getAllUser()).thenThrow(new NotFoundException("Not found any user"));
		this.mvc.perform(get("/api/users/1").accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isNoContent()).andExpect(status().reason("Not found any user"));
		inOrder.verify(userService).setDatabase(1);
		inOrder.verify(userService).getAllUser();
	}

	@Test
	public void testGetAllUsers_db1() throws Exception {
		InOrder inOrder= Mockito.inOrder(userService);
		when(userService.getAllUser())
				.thenReturn(asList(new UserDTO(1l, "nome1", "email1"), new UserDTO(2l, "nome2", "email2")));
		this.mvc.perform(get("/api/users/1").accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("nome1"))).andExpect(jsonPath("$[0].email", is("email1")))
				.andExpect(jsonPath("$[1].id", is(2))).andExpect(jsonPath("$[1].name", is("nome2")))
				.andExpect(jsonPath("$[1].email", is("email2")));
		inOrder.verify(userService).setDatabase(1);
		inOrder.verify(userService).getAllUser();
	}

	@Test
	public void testGetAllUsersEmpty_db2() throws Exception {
		InOrder inOrder= Mockito.inOrder(userService);
		when(userService.getAllUser()).thenThrow(new NotFoundException("Not found any user"));
		this.mvc.perform(get("/api/users/2").accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isNoContent()).andExpect(status().reason("Not found any user"));
		inOrder.verify(userService).setDatabase(2);
		inOrder.verify(userService).getAllUser();
	}

	@Test
	public void testGetAllUsers_db2() throws Exception {
		InOrder inOrder= Mockito.inOrder(userService);
		when(userService.getAllUser())
				.thenReturn(asList(new UserDTO(1l, "nome1", "email1"), new UserDTO(2l, "nome2", "email2")));
		this.mvc.perform(get("/api/users/2").accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("nome1"))).andExpect(jsonPath("$[0].email", is("email1")))
				.andExpect(jsonPath("$[1].id", is(2))).andExpect(jsonPath("$[1].name", is("nome2")))
				.andExpect(jsonPath("$[1].email", is("email2")));
		inOrder.verify(userService).setDatabase(2);
		inOrder.verify(userService).getAllUser();
	}

/////////////////////	
	
	@Test
	public void testGetOneUserByIdWithNoExistingUser_db1() throws Exception {
		InOrder inOrder= Mockito.inOrder(userService);
		when(userService.getUserById(1l)).thenThrow(new NotFoundException("Not found user with id 1"));
		this.mvc.perform(get("/api/users/1/id/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent())
				.andExpect(status().reason("Not found user with id 1"));
		inOrder.verify(userService).setDatabase(1);
		inOrder.verify(userService).getUserById(1l);
	
	}

	@Test
	public void testGetOneUserByIdWithExistingUser_db1() throws Exception {
		InOrder inOrder= Mockito.inOrder(userService);
		when(userService.getUserById(anyLong())).thenReturn(new UserDTO(1l, "nome1", "email1"));
		this.mvc.perform(get("/api/users/1/id/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.name", is("nome1")))
				.andExpect(jsonPath("$.email", is("email1")));
		inOrder.verify(userService).setDatabase(1);
		inOrder.verify(userService).getUserById(1l);
	}
	@Test
	public void testGetOneUserByIdWithNoExistingUser_db2() throws Exception {
		InOrder inOrder= Mockito.inOrder(userService);
		when(userService.getUserById(1l)).thenThrow(new NotFoundException("Not found user with id 1"));
		this.mvc.perform(get("/api/users/2/id/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent())
				.andExpect(status().reason("Not found user with id 1"));
		inOrder.verify(userService).setDatabase(2);
		inOrder.verify(userService).getUserById(1l);
	
	}

	@Test
	public void testGetOneUserByIdWithExistingUser_db2() throws Exception {
		InOrder inOrder= Mockito.inOrder(userService);
		when(userService.getUserById(anyLong())).thenReturn(new UserDTO(1l, "nome1", "email1"));
		this.mvc.perform(get("/api/users/2/id/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.name", is("nome1")))
				.andExpect(jsonPath("$.email", is("email1")));
		inOrder.verify(userService).setDatabase(2);
		inOrder.verify(userService).getUserById(1l);
	}
	
	////////////////////////////

	@Test
	public void testPostInsertNewUser_db1() throws Exception {
		InOrder inOrder= Mockito.inOrder(userService);
		UserDTO userToSave = new UserDTO(null, "nome1", "email1");
		when(userService.insertNewUser(userToSave))
				.thenReturn(new UserDTO(1l, "nome1", "email1"));
		this.mvc.perform(
				post("/api/users/1/new").content(objMapper.writeValueAsString(userToSave))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("nome1"))).andExpect(jsonPath("$.email", is("email1")));
		inOrder.verify(userService).setDatabase(1);
		inOrder.verify(userService).insertNewUser(userToSave);
	}

	@Test
	public void testPostInsertNewUserNull_db1() throws Exception {
		InOrder inOrder= Mockito.inOrder(userService);
		UserDTO userToSave = new UserDTO(null, null, null);
		when(userService.insertNewUser(userToSave))
				.thenThrow(new IllegalArgumentException("User with null property"));
		this.mvc.perform(post("/api/users/1/new").content(objMapper.writeValueAsString(userToSave))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isConflict())
				.andExpect(status().reason("User with null property"));
		inOrder.verify(userService).setDatabase(1);
		inOrder.verify(userService).insertNewUser(userToSave);
	}

	@Test
	public void testPostInsertNewUser_db2() throws Exception {
		InOrder inOrder= Mockito.inOrder(userService);
		UserDTO userToSave = new UserDTO(null, "nome1", "email1");
		when(userService.insertNewUser(userToSave))
				.thenReturn(new UserDTO(1l, "nome1", "email1"));
		this.mvc.perform(
				post("/api/users/2/new").content(objMapper.writeValueAsString(userToSave))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("nome1"))).andExpect(jsonPath("$.email", is("email1")));
		inOrder.verify(userService).setDatabase(2);
		inOrder.verify(userService).insertNewUser(userToSave);
	}

	@Test
	public void testPostInsertNewUserNull_db2() throws Exception {
		InOrder inOrder= Mockito.inOrder(userService);
		UserDTO userToSave = new UserDTO(null, null, null);
		when(userService.insertNewUser(userToSave))
				.thenThrow(new IllegalArgumentException("User with null property"));
		this.mvc.perform(post("/api/users/2/new").content(objMapper.writeValueAsString(userToSave))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isConflict())
				.andExpect(status().reason("User with null property"));
		inOrder.verify(userService).setDatabase(2);
		inOrder.verify(userService).insertNewUser(userToSave);
	}

	@Test
	public void testPutUpdateUser_db1() throws Exception {
		InOrder inOrder= Mockito.inOrder(userService);
		UserDTO userToUpdate = new UserDTO(null, "nome1", "email1");
		when(userService.updateUserById(1, userToUpdate))
				.thenReturn(new UserDTO(1l, "nome1", "email1"));
		this.mvc.perform(
				put("/api/users/1/update/1").content(objMapper.writeValueAsString(userToUpdate))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("nome1"))).andExpect(jsonPath("$.email", is("email1")));
		inOrder.verify(userService).setDatabase(1);
		inOrder.verify(userService).updateUserById(1, userToUpdate);
	}

	@Test
	public void testPutUpdateUserNullProperties_db1() throws Exception {
		InOrder inOrder= Mockito.inOrder(userService);
		UserDTO userToUpdate = new UserDTO(null, null, null);
		when(userService.updateUserById(1, userToUpdate))
				.thenThrow(new IllegalArgumentException("User with null property"));
		this.mvc.perform(put("/api/users/1/update/1").content(objMapper.writeValueAsString(userToUpdate))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isConflict())
				.andExpect(status().reason("User with null property"));
		inOrder.verify(userService).setDatabase(1);
		inOrder.verify(userService).updateUserById(1, userToUpdate);
	}

	@Test
	public void testPutUpdateUserNotExistingUser_db1() throws Exception {
		InOrder inOrder= Mockito.inOrder(userService);
		UserDTO userToUpdate = new UserDTO(null, "nome1", "email1");
		when(userService.updateUserById(1, userToUpdate))
				.thenThrow(new NotFoundException("Try to update not existing user"));
		this.mvc.perform(
				put("/api/users/1/update/1").content(objMapper.writeValueAsString(userToUpdate))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isNotFound())
				.andExpect(status().reason("Try to update not existing user"));
		inOrder.verify(userService).setDatabase(1);
		inOrder.verify(userService).updateUserById(1, userToUpdate);
	}
	@Test
	public void testPutUpdateUser_db2() throws Exception {
		InOrder inOrder= Mockito.inOrder(userService);
		UserDTO userToUpdate = new UserDTO(null, "nome1", "email1");
		when(userService.updateUserById(1, userToUpdate))
				.thenReturn(new UserDTO(1l, "nome1", "email1"));
		this.mvc.perform(
				put("/api/users/2/update/1").content(objMapper.writeValueAsString(userToUpdate))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("nome1"))).andExpect(jsonPath("$.email", is("email1")));
		inOrder.verify(userService).setDatabase(2);
		inOrder.verify(userService).updateUserById(1, userToUpdate);
	}

	@Test
	public void testPutUpdateUserNullProperties_db2() throws Exception {
		InOrder inOrder= Mockito.inOrder(userService);
		UserDTO userToUpdate = new UserDTO(null, null, null);
		when(userService.updateUserById(1, userToUpdate))
				.thenThrow(new IllegalArgumentException("User with null property"));
		this.mvc.perform(put("/api/users/2/update/1").content(objMapper.writeValueAsString(userToUpdate))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isConflict())
				.andExpect(status().reason("User with null property"));
		inOrder.verify(userService).setDatabase(2);
		inOrder.verify(userService).updateUserById(1, userToUpdate);
	}

	@Test
	public void testPutUpdateUserNotExistingUser_db2() throws Exception {
		InOrder inOrder= Mockito.inOrder(userService);
		UserDTO userToUpdate = new UserDTO(null, "nome1", "email1");
		when(userService.updateUserById(1, userToUpdate))
				.thenThrow(new NotFoundException("Try to update not existing user"));
		this.mvc.perform(
				put("/api/users/2/update/1").content(objMapper.writeValueAsString(userToUpdate))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isNotFound())
				.andExpect(status().reason("Try to update not existing user"));
		inOrder.verify(userService).setDatabase(2);
		inOrder.verify(userService).updateUserById(1, userToUpdate);
	}
}
