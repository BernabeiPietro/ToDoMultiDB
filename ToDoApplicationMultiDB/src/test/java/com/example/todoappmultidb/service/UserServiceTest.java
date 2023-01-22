package com.example.todoappmultidb.service;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

import javax.sql.DataSource;

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
import com.example.todoappmultidb.model.dto.UserDTO;
import com.example.todoappmultidb.repository.UserRepository;
import com.example.todoappmultidb.routing.DataSourceContextHolder;
import com.example.todoappmultidb.routing.config.DataSourceEnum;

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
		UserDTO inputDTO=new UserDTO(null, "changed","changed");
	
		
		User retrieved = spy(new User(1L,new ArrayList<>(),"changeIt","changeIt"));
		retrieved.addToDo(new ToDo(1L,retrieved,LocalDateTime.of(1, 2, 3, 1, 2, 12)));
		when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(retrieved));
		
		User userUpdated=new User(1L,new ArrayList<>(), "changed","changed");
		userUpdated.addToDo(new ToDo(1L,userUpdated,LocalDateTime.of(1, 2, 3, 1, 2, 12)));
		when(userRepository.save(any(User.class))).thenReturn(userUpdated);
		
		UserDTO result = userService.updateUserById(1L,inputDTO);

		assertThat(result).isEqualTo(new UserDTO(userUpdated));
		
		InOrder inOrder=inOrder(retrieved,userRepository);
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
	public void test_setContext_one()
	{
		assertThat(userService.setContext(1)).isEqualTo(DataSourceEnum.DATASOURCE_ONE);
	}
	@Test
	public void test_setContext_two()
	{
		assertThat(userService.setContext(2)).isEqualTo(DataSourceEnum.DATASOURCE_TWO);
	}
	@Test
	public void test_setContext_callDataContext()
	{
		userService.setContext(1);
		verify(dataContext).set(any(DataSourceEnum.class));
	}
	@Test
	public void test_fromUser_toDTO() {
		assertThat(userService.toDTO(new User(1l,"prova","prova"))).isEqualTo(new UserDTO(1l,"prova","prova"));
	}
	
}