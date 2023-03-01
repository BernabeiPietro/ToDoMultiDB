package com.example.todoappmultidb.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.todoappmultidb.model.User;
import com.example.todoappmultidb.model.dto.UserDTO;
import com.example.todoappmultidb.repository.UserRepository;
import com.example.todoappmultidb.routing.config.DataSourceRoutingConfiguration;

import io.restassured.RestAssured;
import javassist.NotFoundException;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DataSourceRoutingConfiguration.class)
@EnableConfigurationProperties
@PropertySource("classpath:application.properties")
@SpringBootTest
public class UserServiceRepositoryIT {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	@Before
	public void setup() {
	// always start with an empty database
	userService.setDatabase(1);
	userRepository.deleteAll();
	userRepository.flush();
	userService.setDatabase(2);
	userRepository.deleteAll();
	userRepository.flush();
	}
	@Test
	public void findToDoUserWithinTransaction() throws NotFoundException {

		// setup DB1
		userService.setDatabase(1);
		User toSaveUser = new User(null, new ArrayList<>(), "db1", "db1");
		User savedUser1 = userRepository.save(toSaveUser);

		// setup DB2
		userService.setDatabase(2);
		User toSaveUser2 = new User(null, new ArrayList<>(), "db2", "db2");
		User savedUser2 = userRepository.save(toSaveUser2);

		userService.setDatabase(1);
		assertThat(userService.getUserById(savedUser1.getId())).isEqualTo(new UserDTO(savedUser1));
		assertThat(userService.getAllUser()).doesNotContain(new UserDTO(savedUser2));

		userService.setDatabase(2);
		assertThat(userService.getUserById(savedUser2.getId())).isEqualTo(new UserDTO(savedUser2));
		assertThat(userService.getAllUser()).doesNotContain(new UserDTO(savedUser1));

	}


	@Test
	public void insertToDoUserWithinTransaction_db1() throws NotFoundException {

		userService.setDatabase(1);
		UserDTO user_one_db = new UserDTO(null, "db1_int", "db1_int");
		user_one_db = userService.insertNewUser(user_one_db);

		userService.setDatabase(1);
		assertThat(userService.getUserById(user_one_db.getId())).isEqualTo(user_one_db);

	}

	@Test
	public void insertToDoUserWithinTransaction_db2() throws NotFoundException {

		userService.setDatabase(2);
		UserDTO user_two_db = new UserDTO(null, "db2_int", "db2_int");
		user_two_db = userService.insertNewUser(user_two_db);

		userService.setDatabase(2);
		assertThat(userService.getUserById(user_two_db.getId())).isEqualTo(user_two_db);
	}

	@Test
	public void verifyRollBack_db1() {
		UserDTO nullUser = new UserDTO(null, null, null);

		userService.setDatabase(1);
		int userQta1 = userRepository.findAll().size();

		userService.setDatabase(1);
		assertThrows(IllegalArgumentException.class, () -> userService.insertNewUser(nullUser));
		assertThat(userRepository.findAll()).hasSize(userQta1);

	}

	@Test
	public void verifyRollBack_db2() {
		UserDTO nullUser = new UserDTO(null, null, null);

		userService.setDatabase(2);
		int userQta2 = userRepository.findAll().size();

		userService.setDatabase(2);
		assertThrows(IllegalArgumentException.class, () -> userService.insertNewUser(nullUser));
		assertThat(userRepository.findAll()).hasSize(userQta2);
	}

}
