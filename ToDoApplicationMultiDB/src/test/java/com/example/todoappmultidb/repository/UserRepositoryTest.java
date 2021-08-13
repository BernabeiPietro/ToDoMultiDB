package com.example.todoappmultidb.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.todoappmultidb.model.ToDo;
import com.example.todoappmultidb.model.User;

@DataJpaTest
@RunWith(SpringRunner.class)
public class UserRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserRepository userRepository;

	@Test
	public void test_findAll() {
		User u1 = new User(null, "u1", "test");
		User u2 = new User(null, "u2", "test");
		u1=entityManager.persistFlushFind(u1);
		u2=entityManager.persistFlushFind(u2);
		List<User> u= userRepository.findAll();
		assertThat(u).containsExactlyInAnyOrder(u1, u2);
	}
	@Test
	public void test_findAll_empty() {
		assertThat(userRepository.findAll()).isEmpty();

	}
	
	@Test
	public void test_save_withToDo()
	{
		User u1=new User(null, "u1", "test");
		u1.addToDo(new ToDo());
		User result = userRepository.save(u1);
		assertThat(result).hasNoNullFieldsOrProperties();
		assertThat(result).hasFieldOrPropertyWithValue("name", u1.getName());
		assertThat(result).hasFieldOrPropertyWithValue("email", u1.getEmail());
		assertThat(result).hasFieldOrPropertyWithValue("toDo", u1.getToDo());
	}
	@Test
	public void test_save_withoutToDo()
	{
		User u1=new User(null, "u1", "test");
		User result = userRepository.save(u1);
		assertThat(result).hasNoNullFieldsOrProperties();
		assertThat(result).hasFieldOrPropertyWithValue("name", u1.getName());
		assertThat(result).hasFieldOrPropertyWithValue("email", u1.getEmail());
		assertThat(result).hasFieldOrPropertyWithValue("toDo", u1.getToDo());
	}
		
	
	@Test
	public void test_update()
	{
		User u1=new User(null, "u1", "test");
		u1.addToDo(new ToDo());
		entityManager.persistFlushFind(u1);
		u1.setEmail("change");
		u1.setName("changed");
		u1.addToDo(new ToDo());
		User changed=userRepository.save(u1);
		assertThat(changed).hasNoNullFieldsOrProperties();
		assertThat(changed).hasFieldOrPropertyWithValue("name", u1.getName());
		assertThat(changed).hasFieldOrPropertyWithValue("email", u1.getEmail());
		assertThat(changed.getToDo()).containsAll(u1.getToDo());
	}
	@Test
	public  void test_findById() {
		User u1=new User(null, "u1", "test");
		User u2=new User(null, "u2", "test");
		entityManager.persistFlushFind(u1);
		entityManager.persistFlushFind(u2);
		User result=userRepository.findById(u1.getId()).get();
		assertThat(result).hasNoNullFieldsOrProperties();
		assertThat(result).hasFieldOrPropertyWithValue("name", u1.getName());
		assertThat(result).hasFieldOrPropertyWithValue("email", u1.getEmail());
		assertThat(result.getToDo()).containsAll(u1.getToDo());
	}
}
