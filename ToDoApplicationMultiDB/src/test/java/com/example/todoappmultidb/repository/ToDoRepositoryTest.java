package com.example.todoappmultidb.repository;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.todoappmultidb.model.ToDo;
import com.example.todoappmultidb.model.User;


@DataJpaTest
@RunWith(SpringRunner.class)
public class ToDoRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ToDoRepository toDoRepository;

	private ToDo todo1;
	private ToDo todo2;
	private User u;

	@Before
	public void setup() {
		Map<String, Boolean> m1 = new HashMap<String, Boolean>();
		m1.put("todo1", false);
		Map<String, Boolean> m2 = new HashMap<String, Boolean>();
		m2.put("todo2", true);
		u = new User(null, "nome1", "email1");
		todo1 = new ToDo(null, u, m1, LocalDateTime.of(2012, Month.DECEMBER, 12, 0, 0));
		todo2 = new ToDo(null, u, m2, LocalDateTime.of(2014, Month.NOVEMBER, 15, 0, 0));
		u.getToDo().addAll(asList(todo1,todo2));
	}

	@Test
	public void test_findAll_nothing() {
		assertThat(toDoRepository.findAll()).isEmpty();
	}

	@Test
	public void test_findAll() {
		entityManager.persistAndFlush(u);
		ToDo result1 = entityManager.persistAndFlush(todo1);
		ToDo result2 = entityManager.persistAndFlush(todo2);
		assertThat(toDoRepository.findAll()).containsExactly(result1, result2);
		
	}

	@Test
	public void test_findToDoWithUserId() {
		User user=entityManager.persistAndFlush(u);
		ToDo result1 = entityManager.persistAndFlush(todo1);
		ToDo result2 = entityManager.persistAndFlush(todo2);
		assertThat(toDoRepository.findToDoByUserId(u)).isEqualTo(asList(result1,result2));
	}

	@Test
	public void test_findToDoWithUserId_nothing() {
		User user=entityManager.persistAndFlush(new User(null, "name1", "name2"));
		assertThat(toDoRepository.findToDoByUserId(user)).isEmpty();
	}

	@Test
	public void test_findById_found() {
		entityManager.persistAndFlush(u);
		ToDo insert = entityManager.persistAndFlush(todo1);
		assertThat(toDoRepository.findById(insert.getId())).hasValue(insert);
	}

	@Test
	public void test_findById_notFound() {
		entityManager.persistAndFlush(u);
		entityManager.persistAndFlush(todo1);
		assertThat(toDoRepository.findById(0L)).isEmpty();
	}

	@Test
	public void test_save() {
		entityManager.persistFlushFind(u);
		ToDo result = toDoRepository.save(todo1);
		assertThat(result).hasNoNullFieldsOrProperties();
		assertThat(result).hasFieldOrPropertyWithValue("toDo", todo1.getToDo());
		assertThat(result).hasFieldOrPropertyWithValue("idOfUser", todo1.getIdOfUser());
		assertThat(result).hasFieldOrPropertyWithValue("date", LocalDateTime.of(2012, Month.DECEMBER, 12, 0, 0));
	}

	@Test
	public void test_updateUserAndTodo() {
		Map<String, Boolean> mapToUpdate = new HashMap<String, Boolean>();
		mapToUpdate.put("todo1", false);
		User userToUpdate = entityManager.persistAndFlush(new User(null,"nome1","email1"));
		User userUpdated = entityManager.persistAndFlush(new User(null,null,null));
		ToDo result = entityManager.persistFlushFind(
				new ToDo(null, userToUpdate, mapToUpdate, LocalDateTime.of(2012, Month.DECEMBER, 12, 0, 0)));
		result.addToDoAction("todo2", false);
		result.setIdOfUser(userUpdated);
		ToDo saved = toDoRepository.save(result);
		assertThat(saved.getId()).isEqualTo(result.getId());
		assertThat(saved.getIdOfUser()).isEqualTo(userUpdated);
		assertThat(saved.getLocalDateTime()).isEqualTo(LocalDateTime.of(2012, Month.DECEMBER, 12, 0, 0));
		assertThat(saved.getToDo()).isEqualTo(result.getToDo());
	}
	@Test
	public void test_updateLimitedInformationOfUser() {
		Map<String, Boolean> mapToUpdate = new HashMap<String, Boolean>();
		mapToUpdate.put("todo1", false);
		User userToUpdate = entityManager.persistAndFlush(new User(null,"nome1","email1"));
		ToDo result = entityManager.persistFlushFind(
				new ToDo(null, userToUpdate, mapToUpdate, LocalDateTime.of(2012, Month.DECEMBER, 12, 0, 0)));
		User userUpdated = new User(userToUpdate.getId(), null, null);
		result.setIdOfUser(userUpdated);
		ToDo saved = toDoRepository.save(result);
		assertThat(saved.getId()).isEqualTo(result.getId());
		assertThat(saved.getIdOfUser()).isEqualTo(userUpdated);
		assertThat(saved.getLocalDateTime()).isEqualTo(LocalDateTime.of(2012, Month.DECEMBER, 12, 0, 0));
		assertThat(saved.getToDo()).isEqualTo(result.getToDo());
	}
	@Test
	public void test_retrieveUserListOfTodoWithALimitedUserId() {
		User user=entityManager.persistAndFlush(u);
		ToDo result1 = entityManager.persistAndFlush(todo1);
		ToDo result2 = entityManager.persistAndFlush(todo2);
		assertThat(toDoRepository.findToDoByUserId(new User(user.getId(), null, null))).isEqualTo(asList(result1,result2));
	}
	@Test
	public void test_retrieveUserListOfToDo_UserNotExist() {
		User user=entityManager.persistAndFlush(u);
		ToDo result1 = entityManager.persistAndFlush(todo1);
		ToDo result2 = entityManager.persistAndFlush(todo2);
		User u2 = new User(user.getId()+1, null, null);
		assertThat(entityManager.find(User.class, u2.getId())).isEqualTo(null);
		assertThat(toDoRepository.findToDoByUserId(u2)).isEqualTo(Collections.emptyList());
	}
	
}
