package com.example.todoappmultidb.service;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.example.todoappmultidb.model.User;
import com.example.todoappmultidb.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@Test
	public void test_getAllUser_empty() {
		when(userRepository.findAll()).thenReturn(Collections.emptyList());
		assertThat(userService.getAllUser()).isEmpty();
	}

	@Test
	public void test_getAllUser() {
		User user1 = new User(1L, "first", "email1");
		User user2 = new User(2L, "second", "email2");
		when(userRepository.findAll()).thenReturn(asList(user1, user2));
		assertThat(userService.getAllUser()).containsExactly(user1, user2);
	}

	@Test
	public void test_getUserById_found() {
		User user = new User(1L, "first", "email1");
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		assertThat(userService.getUserById(1L)).isSameAs(user);
	}
	@Test
	public void test_getUserById_notfound() {
		when(userRepository.findById(1L)).thenReturn(Optional.empty());
		assertThat(userService.getUserById(1L)).isNull();
	}
	
	@Test
	public void test_insertNewUser_nullId_returnSavedUser() {
		User userToSave=spy(new User(99L,"",""));
		User saved=new User(1L,"saved","saved");
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
		User userToUpdate=spy(new User(null,"change","change"));
		User userUpdated=new User(1L,"changed","changed");
		when(userRepository.save(any(User.class))).thenReturn(userUpdated);
		User result = userService.updateUserById(1L,userToUpdate);
		assertThat(result).isSameAs(userUpdated);
		InOrder inOrder=inOrder(userToUpdate,userRepository);
		inOrder.verify(userToUpdate).setId(1L);
		inOrder.verify(userRepository).save(userToUpdate);

	}
}