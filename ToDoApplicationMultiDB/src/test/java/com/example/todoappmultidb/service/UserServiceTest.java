package com.example.todoappmultidb.service;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.example.todoappmultidb.model.ToDo;
import com.example.todoappmultidb.model.User;
import com.example.todoappmultidb.model.dto.ToDoDTO;
import com.example.todoappmultidb.model.dto.UserDTO;
import com.example.todoappmultidb.repository.UserRepository;
import com.example.todoappmultidb.routing.DataSourceContextHolder;

import javassist.NotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;
	@Spy
	private DataSourceContextHolder dataContext;

	@Test
	public void test_getAllUser_empty() {
		when(userRepository.findAll()).thenReturn(Collections.emptyList());
		Exception thrown= assertThrows(NotFoundException.class,() -> userService.getAllUser());
		assertThat(thrown.getMessage()).isEqualTo("Not found any User");
		
	}
	@Test
	public void test_getAllUser_null() {
		when(userRepository.findAll()).thenReturn(null);
		Exception thrown= assertThrows(NullPointerException.class,() -> userService.getAllUser());
		
	}
	@Test
	public void test_getAllUser() throws NotFoundException {
		User user1 = new User(1L, new ArrayList<>(), "first", "email1");
		User user2 = new User(2L, new ArrayList<>(), "second", "email2");
		when(userRepository.findAll()).thenReturn(asList(user1, user2));
		assertThat(userService.getAllUser()).containsExactly(new UserDTO(user1), new UserDTO(user2));
	}
	
	@Test
	public void test_getUserById_found() throws NotFoundException {
		User user = new User(1L, new ArrayList<>(), "first", "email1");
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		assertThat(userService.getUserById(1L)).isEqualTo(new UserDTO(user));
	}
	@Test
	public void test_getUserById_notfound() {
		when(userRepository.findById(1L)).thenReturn(Optional.empty());
		Exception thrown= assertThrows(NotFoundException.class,() -> userService.getUserById(1L));
		assertThat(thrown.getMessage()).isEqualTo("Not found any User");
	}
	@Test
	public void test_getUserById_null() {
		when(userRepository.findById(1L)).thenReturn(null);
		Exception thrown= assertThrows(NullPointerException.class,() -> userService.getUserById(1L));
	}
	@Test
	public void test_insertNewUser_nullId_returnSavedUser() {
		UserDTO userToSave=spy(new UserDTO(99L, "prova","prova"));
		User saved=new User(1L,new ArrayList<>(), "saved","saved");
		when(userRepository.save(any(User.class)))
		.thenReturn(saved);
		UserDTO result = userService.insertNewUser(userToSave);
		assertThat(result).isEqualTo(new UserDTO(1L, "saved","saved") );
		InOrder inOrder=Mockito.inOrder(userToSave,userRepository);
		inOrder.verify(userToSave).setId(null);
		inOrder.verify(userRepository).save(any(User.class));
	}
	@Test
	public void test_save_nullvalue() {
		UserDTO nullUser=new UserDTO(null,null,null);
		Exception thrown=assertThrows(IllegalArgumentException.class, ()->userService.insertNewUser(nullUser));
		verify(userRepository,never()).save(any(User.class));
		assertThat(thrown.getMessage()).isEqualTo("User with null property");
		
	}
	@Test
	public void test_updateUserById() throws NotFoundException {
		UserDTO inputDTO=spy(new UserDTO(null, "changed","changed"));
	
		
		User retrieved = spy(new User(1L,new ArrayList<>(),"changeIt","changeIt"));
		retrieved.addToDo(new ToDo(1L,retrieved,LocalDateTime.of(1, 2, 3, 1, 2, 12)));
		when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(retrieved));
		
		User userUpdated=new User(1L,new ArrayList<>(), "changed","changed");
		userUpdated.addToDo(new ToDo(1L,userUpdated,LocalDateTime.of(1, 2, 3, 1, 2, 12)));
		when(userRepository.save(any(User.class))).thenReturn(userUpdated);
		
		UserDTO result = userService.updateUserById(1L,inputDTO);

		assertThat(result).isEqualTo(new UserDTO(userUpdated));
		
		InOrder inOrder=inOrder(inputDTO,retrieved,userRepository);
		inOrder.verify(inputDTO).setId(any(Long.class));
		inOrder.verify(retrieved).setEmail("changed");
		inOrder.verify(retrieved).setName("changed");
		inOrder.verify(retrieved,never()).setTodo(any(ArrayList.class));
		inOrder.verify(userRepository).save(retrieved);
	}
	@Test
	public void test_updateUserById_nullvalue() {
		UserDTO nullUser=new UserDTO(null,null,null);
		Exception thrown=assertThrows(IllegalArgumentException.class, ()->userService.updateUserById(1L,nullUser));
		verify(userRepository,never()).save(any(User.class));	
		assertThat(thrown.getMessage()).isEqualTo("User with null property");

	}
	@Test
	public void test_updateUserById_notFindUser() {
		UserDTO userToUpdate=spy(new UserDTO(null, "change","change"));
		Exception thrown=assertThrows(NotFoundException.class, ()->userService.updateUserById(1L,userToUpdate));
		verify(userRepository,never()).save(any(User.class));	
		assertThat(thrown.getMessage()).isEqualTo("Not found any User");

	}
	@Test
	public void test_getToDoOfUser() throws NotFoundException {
		User user = new User(1L,"first", "email1");
		ToDo todo1 = new ToDo(1L, user, new HashMap<String, Boolean>(), LocalDateTime.of(2012, 3, 5, 0, 0));
		ToDo todo2 = new ToDo(2L, user, new HashMap<String, Boolean>(), LocalDateTime.of(2013, 6, 7, 0, 0));
		user.addToDo(todo1);
		user.addToDo(todo2);
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		assertThat(userService.getToDoOfUser(1L)).isEqualTo(asList(new ToDoDTO(todo1),new ToDoDTO(todo2)));
	}
	@Test
	public void test_getToDoOfUser_notfound() {
		when(userRepository.findById(1L)).thenReturn(Optional.empty());
		Exception thrown = assertThrows(NotFoundException.class, () -> userService.getToDoOfUser(1L));
		assertThat(thrown.getMessage()).isEqualTo("Not found any User");
	}

	@Test
	public void test_getToDoOfUser_null() {
		when(userRepository.findById(1L)).thenReturn(null);
		Exception thrown = assertThrows(NullPointerException.class, () -> userService.getToDoOfUser(1L));
	}
	@Test
	public void test_fromUser_toDTO() {
		assertThat(userService.toDTO(new User(1l,"prova","prova"))).isEqualTo(new UserDTO(1l,"prova","prova"));
	}
	@Test
	public void test_clearContex() {
		userService.clearContext();
		verify(dataContext).clear();
	}
	
}