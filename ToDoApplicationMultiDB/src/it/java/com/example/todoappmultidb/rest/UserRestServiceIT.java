package com.example.todoappmultidb.rest;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.todoappmultidb.model.User;
import com.example.todoappmultidb.model.dto.UserDTO;
import com.example.todoappmultidb.repository.UserRepository;
import com.example.todoappmultidb.service.UserService;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserRestServiceIT {

	@Autowired
	UserService userService;
	@Autowired
	UserRepository userRepository;
	@LocalServerPort
	private int port;
	
	@Before
	public void setup() {
	RestAssured.port = port;
	// always start with an empty database
	userService.setDatabase(1);
	userRepository.deleteAll();
	userRepository.flush();
	userService.setDatabase(2);
	userRepository.deleteAll();
	userRepository.flush();
	}
	

	@Test
	public void testPost_NewUser_db1() throws Exception {
		Response response = given().
				contentType(MediaType.APPLICATION_JSON_VALUE).
				body(new UserDTO(null, "nome1", "email1")).
				when().
				post("/api/users/1/new");
		UserDTO userSaved =response.getBody().as(UserDTO.class);
		userService.setDatabase(1);
		assertThat(new UserDTO(userRepository.findById(userSaved.getId()).get())).isEqualTo(userSaved);
		userService.setDatabase(2);
		assertThat(userRepository.findById(userSaved.getId()).isPresent()).isFalse();
	}
	@Test
	public void testPost_NewUser_db2() throws Exception {
		Response response = given().
				contentType(MediaType.APPLICATION_JSON_VALUE).
				body(new UserDTO(null, "nome2", "email2")).
				when().
				post("/api/users/2/new");
		UserDTO userSaved =response.getBody().as(UserDTO.class);
		userService.setDatabase(2);
		assertThat(new UserDTO(userRepository.findById(userSaved.getId()).get())).isEqualTo(userSaved);
		userService.setDatabase(1);
		assertThat(userRepository.findById(userSaved.getId())).isEmpty();
	}
	
	@Test
	public void testPut_UpdateUser_db2() {
		userService.setDatabase(2);
		User saved=userRepository.save(new User(1L, "2emon", "2emon"));
		Response response = given().
				contentType(MediaType.APPLICATION_JSON_VALUE).
				body(new UserDTO(1L, "nome2", "email2")).
				when().
				put("/api/users/2/update/"+saved.getId());
		UserDTO userSaved =response.getBody().as(UserDTO.class);
		userService.setDatabase(2);
		assertThat(new UserDTO(userRepository.findById(userSaved.getId()).get())).isEqualTo(userSaved);
		userService.setDatabase(1);
		assertThat(userRepository.findById(userSaved.getId())).isEmpty();
		}
	@Test
	public void testPut_UpdateUser_db1() {
		userService.setDatabase(1);
		User saved=userRepository.save(new User(1L, "1emon", "1emon"));
		Response response = given().
				contentType(MediaType.APPLICATION_JSON_VALUE).
				body(new UserDTO(1L, "nome1", "email1")).
				when().
				put("/api/users/1/update/"+saved.getId());
		UserDTO userSaved =response.getBody().as(UserDTO.class);
		userService.setDatabase(1);
		assertThat(new UserDTO(userRepository.findById(userSaved.getId()).get())).isEqualTo(userSaved);
		userService.setDatabase(2);
		assertThat(userRepository.findById(userSaved.getId())).isEmpty();
		}
	
	
}
