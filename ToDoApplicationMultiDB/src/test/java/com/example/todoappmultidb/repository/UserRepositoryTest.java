package com.example.todoappmultidb.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.todoappmultidb.model.ToDo;
import com.example.todoappmultidb.model.User;

@ActiveProfiles("repository")
@DataJpaTest
@RunWith(SpringRunner.class)
public class UserRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserRepository userRepository;

	@Test
	public void test_findAll() {
		User u1 = new User(null, new ArrayList<>(), "u1", "test");
		User u2 = new User(null, new ArrayList<>(), "u2", "test");
		u1 = entityManager.persistFlushFind(u1);
		u2 = entityManager.persistFlushFind(u2);
		List<User> u = userRepository.findAll();
		assertThat(u).containsExactlyInAnyOrder(u1, u2);
	}

	@Test
	public void test_findAll_empty() {
		assertThat(userRepository.findAll()).isEmpty();

	}

	@Test
	public void test_save_withToDo() {
		User u1 = new User(null, new ArrayList<>(), "u1", "test");
		u1.addToDo(new ToDo());
		User result = userRepository.save(u1);
		assertThat(result).hasNoNullFieldsOrProperties().hasFieldOrPropertyWithValue("name", u1.getName())
				.hasFieldOrPropertyWithValue("email", u1.getEmail()).hasFieldOrPropertyWithValue("toDo", u1.getToDo());
	}

	@Test
	public void test_save_withoutToDo() {
		User u1 = new User(null, new ArrayList<>(), "u1", "test");
		User result = userRepository.save(u1);
		assertThat(result).hasNoNullFieldsOrProperties().hasFieldOrPropertyWithValue("name", u1.getName())
				.hasFieldOrPropertyWithValue("email", u1.getEmail()).hasFieldOrPropertyWithValue("toDo", u1.getToDo());
	}

	@Test
	public void test_update() {
		User u1 = new User(null, new ArrayList<>(), "u1", "test");
		User result = entityManager.persistFlushFind(u1);
		ToDo td = entityManager
				.persistFlushFind(new ToDo(null, result, new HashMap<>(), LocalDateTime.of(2003, 1, 1, 0, 0)));
		result.addToDo(td);
		result.setEmail("changed");
		result.setName("changed");
		result.addToDo(new ToDo(null, result, new HashMap<>(), LocalDateTime.of(2005, 2, 1, 0, 0)));
		User changed = userRepository.save(result);
		assertThat(changed).hasNoNullFieldsOrProperties().hasFieldOrPropertyWithValue("name", "changed")
				.hasFieldOrPropertyWithValue("email", "changed");
		assertThat(changed.getToDo()).containsAll(result.getToDo());
	}

	@Test
	public void test_findById() {
		User u1 = new User(null, new ArrayList<>(), "u1", "test");
		User u2 = new User(null, new ArrayList<>(), "u2", "test");

		User persisted = entityManager.persistFlushFind(u1);
		persisted.addToDo(entityManager
				.persistFlushFind(new ToDo(null, u1, new HashMap<>(), LocalDateTime.of(2003, 1, 1, 0, 0))));
		entityManager.persistFlushFind(u2);
		User result = userRepository.findById(persisted.getId()).get();
		assertThat(result).hasNoNullFieldsOrProperties().hasFieldOrPropertyWithValue("name", persisted.getName())
				.hasFieldOrPropertyWithValue("email", persisted.getEmail());
		assertThat(result.getToDo()).containsAll(persisted.getToDo());
	}
}
