package com.example.todoappmultidb.service;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
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

import com.example.todoappmultidb.model.User;
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
		assertThat(userService.getAllUser()).containsExactly(user1, user2);
	}
	
	@Test
	public void test_getUserById_found() throws NotFoundException {
		User user = new User(1L, new ArrayList<>(), "first", "email1");
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		assertThat(userService.getUserById(1L)).isSameAs(user);
	}
	@Test
	public void test_getUserById_notfound() {
		when(userRepository.findById(1L)).thenReturn(Optional.empty());
		Exception thrown= assertThrows(NotFoundException.class,() -> userService.getUserById(1L));
		assertThat(thrown.getMessage()).isEqualTo("Not found any User");
	}
	
	@Test
	public void test_insertNewUser_nullId_returnSavedUser() {
		User userToSave=spy(new User(99L,new ArrayList<>(), "",""));
		User saved=new User(1L,new ArrayList<>(), "saved","saved");
		when(userRepository.save(any(User.class)))
		.thenReturn(saved);
		User result = userService.insertNewUser(userToSave);
		assertThat(result).isSameAs(saved);
		InOrder inOrder=Mockito.inOrder(userToSave,userRepository);
		inOrder.verify(userToSave).setId(null);
		inOrder.verify(userRepository).save(userToSave);
	}
	@Test
	public void test_updateUser_ById() {
		User userToUpdate=spy(new User(null,new ArrayList<>(), "change","change"));
		User userUpdated=new User(1L,new ArrayList<>(), "changed","changed");
		when(userRepository.save(any(User.class))).thenReturn(userUpdated);
		User result = userService.updateUserById(1L,userToUpdate);
		assertThat(result).isSameAs(userUpdated);
		InOrder inOrder=inOrder(userToUpdate,userRepository);
		inOrder.verify(userToUpdate).setId(1L);
		inOrder.verify(userRepository).save(userToUpdate);
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
}