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
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.example.todoappmultidb.model.ToDo;
import com.example.todoappmultidb.model.User;
import com.example.todoappmultidb.repository.ToDoRepository;

import javassist.NotFoundException;


@RunWith(MockitoJUnitRunner.class)
public class ToDoServiceTest {

		@Mock
		ToDoRepository toDoRepository;
		@InjectMocks
		ToDoService toDoService;

		@Test
		public void test_findAll_empty() {
			when(toDoRepository.findAll()).thenReturn(Collections.emptyList());
			Exception thrown= assertThrows(NotFoundException.class,() -> toDoService.findAll());
			assertThat(thrown.getMessage()).isEqualTo("Not found any ToDo");
		}

		@Test
		public void test_findAll() throws NotFoundException {
		
			ToDo todo1 = new ToDo(1L, new User(), new HashMap<String, Boolean>(), LocalDateTime.of(2012,3,5, 0, 0));
			ToDo todo2 = new ToDo(2L, new User(), new HashMap<String, Boolean>(), LocalDateTime.of(2013,6,7, 0, 0));
			when(toDoRepository.findAll()).thenReturn(asList(todo1, todo2));
			assertThat(toDoService.findAll()).containsExactly(todo1, todo2);
		}

		@Test
		public void test_findById_found() throws NotFoundException {
			ToDo todo1 = new ToDo(1L, new User(), new HashMap<String, Boolean>(),  LocalDateTime.of(2012,3,5, 0, 0));
			when(toDoRepository.findById(1L)).thenReturn(Optional.of(todo1));
			assertThat(toDoService.findById(1L)).isEqualTo(todo1);
		}

		@Test
		public void test_findById_notfound() {
			when(toDoRepository.findById(1L)).thenReturn(Optional.empty());
			Exception thrown= assertThrows(NotFoundException.class,() -> toDoService.findById(1L));
			assertThat(thrown.getMessage()).isEqualTo("Not found any ToDo");
		}

		@Test
		public void test_save() {
			User u=new User();
			ToDo toSave = spy(new ToDo(null, u, new HashMap<String, Boolean>(),  LocalDateTime.of(2012,3,5, 0, 0)));
			ToDo saved = new ToDo(1L, u, new HashMap<String, Boolean>(), LocalDateTime.of(2012,3,5, 0, 0));
			when(toDoRepository.save(any(ToDo.class))).thenReturn(saved);
			ToDo result = toDoService.save(toSave);
			assertThat(result).isSameAs(saved);
			InOrder inOrder = inOrder(toSave, toDoRepository);
			inOrder.verify(toDoRepository).save(toSave);
		}

		@Test
		public void test_save_nullvalue() {
			ToDo nullToDo=new ToDo(null, null, null, null);
			Exception thrown=assertThrows(IllegalArgumentException.class, ()->toDoService.save(nullToDo));
			verify(toDoRepository,never()).save(any(ToDo.class));
			assertThat(thrown.getMessage()).isEqualTo("ToDo with null property");
		}
		@Test
		public void test_updateById_nullvalue() {
			ToDo nullToDo=new ToDo(null, null, null, null);
			Exception thrown= assertThrows(IllegalArgumentException.class, ()->toDoService.updateById(1L,nullToDo));
			verify(toDoRepository,never()).save(any(ToDo.class));	
			assertThat(thrown.getMessage()).isEqualTo("ToDo with null property");
		}
		@Test
		public void test_updateById() {
			User u=new User();
			ToDo toUpdate = spy(new ToDo(99L, u, new HashMap<String, Boolean>(),  LocalDateTime.of(2012,3,5, 0, 0)));
			ToDo update = new ToDo(1L, u, new HashMap<String, Boolean>(),  LocalDateTime.of(2012,3,5, 0, 0));
			when(toDoRepository.save(any(ToDo.class))).thenReturn(update);
			ToDo result = toDoService.updateById(1L, toUpdate);
			assertThat(result).isSameAs(update);
			InOrder inOrder = inOrder(toUpdate, toDoRepository);
			inOrder.verify(toUpdate).setId(1L);
			inOrder.verify(toDoRepository).save(toUpdate);
		}
	}

