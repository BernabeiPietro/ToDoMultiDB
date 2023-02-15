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
import com.example.todoappmultidb.repository.ToDoRepository;
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
	userService.setContext(1);
	userRepository.deleteAll();
	userRepository.flush();
	userService.setContext(2);
	userRepository.deleteAll();
	userRepository.flush();
	}
	
	/*
	 * @Test public void testPost_ToDo() throws JsonProcessingException, Exception {
	 * userService.setContext(1); User toSaveUser = new User(null, new
	 * ArrayList<>(), "db1", "db1"); User savedUser1 =
	 * userRepository.save(toSaveUser); userService.setContext(2); User toSaveUser2
	 * = new User(null, new ArrayList<>(), "db2", "db2"); User savedUser2 =
	 * userRepository.save(toSaveUser2); HashMap<String, Boolean> actions = new
	 * HashMap<String, Boolean>(); actions.put("first", false);
	 * actions.put("second", true); ToDoDTO todo1 = new ToDoDTO(null,
	 * savedUser1.getId(), actions, LocalDateTime.of(2000, 5, 13, 1, 1, 1)); ToDoDTO
	 * todo2 = new ToDoDTO(null, savedUser2.getId(), actions, LocalDateTime.of(2000,
	 * 5, 13, 1, 1, 1));
	 * 
	 * this.mvc.perform(post("/api/todo/1/new").content(objMapper.writeValueAsString
	 * (todo1))
	 * .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
	 * .andDo(MockMvcResultHandlers.print()).andExpect(status().isCreated())
	 * .andExpect(jsonPath("$.id", notNullValue())) .andExpect(jsonPath("$.date",
	 * is(LocalDateTime.of(2000, 5, 13, 1, 1, 1).toString())))
	 * .andExpect(jsonPath("$.idOfUser", is(todo1.getIdOfUser().intValue())))
	 * .andExpect(jsonPath("$.toDo", is(actions)));
	 * 
	 * this.mvc.perform(post("/api/todo/2/new").content(objMapper.writeValueAsString
	 * (todo2))
	 * .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
	 * .andDo(MockMvcResultHandlers.print()).andExpect(status().isCreated())
	 * .andExpect(jsonPath("$.id", notNullValue())) .andExpect(jsonPath("$.date",
	 * is(LocalDateTime.of(2000, 5, 13, 1, 1, 1).toString())))
	 * .andExpect(jsonPath("$.idOfUser", is(todo2.getIdOfUser().intValue())))
	 * .andExpect(jsonPath("$.toDo", is(actions))); }
	 */
	@Test
	public void testPost_NewUser_db1() throws Exception {
		Response response = given().
				contentType(MediaType.APPLICATION_JSON_VALUE).
				body(new UserDTO(null, "nome1", "email1")).
				when().
				post("/api/users/1/new");
		UserDTO userSaved =response.getBody().as(UserDTO.class);
		userService.setContext(1);
		assertThat(new UserDTO(userRepository.findById(userSaved.getId()).get())).isEqualTo(userSaved);
		userService.setContext(2);
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
		userService.setContext(2);
		assertThat(new UserDTO(userRepository.findById(userSaved.getId()).get())).isEqualTo(userSaved);
		userService.setContext(1);
		assertThat(userRepository.findById(userSaved.getId())).isEmpty();
	}
	
	@Test
	public void testPut_UpdateUser_db2() {
		userService.setContext(2);
		User saved=userRepository.save(new User(1L, "2emon", "2emon"));
		Response response = given().
				contentType(MediaType.APPLICATION_JSON_VALUE).
				body(new UserDTO(1L, "nome2", "email2")).
				when().
				put("/api/users/2/update/"+saved.getId());
		UserDTO userSaved =response.getBody().as(UserDTO.class);
		userService.setContext(2);
		assertThat(new UserDTO(userRepository.findById(userSaved.getId()).get())).isEqualTo(userSaved);
		userService.setContext(1);
		assertThat(userRepository.findById(userSaved.getId())).isEmpty();
		}
	@Test
	public void testPut_UpdateUser_db1() {
		userService.setContext(1);
		User saved=userRepository.save(new User(1L, "1emon", "1emon"));
		Response response = given().
				contentType(MediaType.APPLICATION_JSON_VALUE).
				body(new UserDTO(1L, "nome1", "email1")).
				when().
				put("/api/users/1/update/"+saved.getId());
		UserDTO userSaved =response.getBody().as(UserDTO.class);
		userService.setContext(1);
		assertThat(new UserDTO(userRepository.findById(userSaved.getId()).get())).isEqualTo(userSaved);
		userService.setContext(2);
		assertThat(userRepository.findById(userSaved.getId())).isEmpty();
		}
	
	/*
	 * @Test public void testGetFindToDo() throws Exception {
	 * 
	 * userService.setContext(1); User toSaveUser = new User(null, new
	 * ArrayList<>(), "db1", "db1"); ToDo toSaveTodo = new ToDo(null, toSaveUser,
	 * new HashMap<>(), LocalDateTime.of(2005, 1, 1, 1, 1, 1));
	 * toSaveTodo.addToDoAction("prova", false); userRepository.save(toSaveUser);
	 * ToDo savedTodo1 = todoRepository.save(toSaveTodo);
	 * this.mvc.perform(get("/api/todo/1/id/" +
	 * savedTodo1.getId().toString()).contentType(MediaType.APPLICATION_JSON)
	 * .accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).
	 * andExpect(status().isOk()) .andExpect(jsonPath("$.id",
	 * is(savedTodo1.getId().intValue()))) .andExpect(jsonPath("$.date",
	 * is(savedTodo1.getLocalDateTime().toString())))
	 * .andExpect(jsonPath("$.idOfUser",
	 * is(savedTodo1.getIdOfUser().getId().intValue())))
	 * .andExpect(jsonPath("$.toDo", is(savedTodo1.getToDo())));
	 * userService.setContext(2); User toSaveUser2 = new User(null, new
	 * ArrayList<>(), "db1", "db1"); ToDo toSaveTodo2 = new ToDo(null, toSaveUser2,
	 * new HashMap<>(), LocalDateTime.of(2005, 1, 1, 1, 1, 1));
	 * toSaveTodo2.addToDoAction("prova", false); userRepository.save(toSaveUser2);
	 * ToDo savedTodo2 = todoRepository.save(toSaveTodo2);
	 * this.mvc.perform(get("/api/todo/2/id/" +
	 * savedTodo2.getId().toString()).contentType(MediaType.APPLICATION_JSON)
	 * .accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).
	 * andExpect(status().isOk()) .andExpect(jsonPath("$.id",
	 * is(savedTodo2.getId().intValue()))) .andExpect(jsonPath("$.date",
	 * is(savedTodo2.getLocalDateTime().toString())))
	 * .andExpect(jsonPath("$.idOfUser",
	 * is(savedTodo2.getIdOfUser().getId().intValue())))
	 * .andExpect(jsonPath("$.toDo", is(savedTodo2.getToDo()))); }
	 * 
	 * @Test public void testGetOneUser() throws Exception {
	 * userService.setContext(1); User savedUser1 = userRepository.save(new
	 * User(null, new ArrayList<>(), "db1", "db1"));
	 * this.mvc.perform(get("/api/users/1/id/" +
	 * savedUser1.getId().toString()).accept(MediaType.APPLICATION_JSON))
	 * .andExpect(status().isOk()).andExpect(jsonPath("$.id",
	 * is(savedUser1.getId().intValue()))) .andExpect(jsonPath("$.name",
	 * is(savedUser1.getName()))) .andExpect(jsonPath("$.email",
	 * is(savedUser1.getEmail()))); userService.setContext(2); User savedUser2 =
	 * userRepository.save(new User(null, new ArrayList<>(), "db1", "db1"));
	 * this.mvc.perform(get("/api/users/1/id/" +
	 * savedUser2.getId().toString()).accept(MediaType.APPLICATION_JSON))
	 * .andExpect(status().isOk()).andExpect(jsonPath("$.id",
	 * is(savedUser2.getId().intValue()))) .andExpect(jsonPath("$.name",
	 * is(savedUser2.getName()))) .andExpect(jsonPath("$.email",
	 * is(savedUser2.getEmail()))); }
	 */
}
