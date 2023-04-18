package com.example.todoappmultidb.webcontroller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.todoappmultidb.model.dto.ToDoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ToDoWebControllerE2E {// NOSONAR
	private static int port = Integer.parseInt(System.getProperty("server.port", "8080"));
	private static String baseUrl = "http://localhost:" + port;
	private WebDriver driver;

	@BeforeClass
	public static void setupClass() {
		WebDriverManager.chromedriver().setup();
	}

	@Before
	public void setup() {
		baseUrl = "http://localhost:" + port;
		driver = new ChromeDriver();
	}

	@After
	public void teardown() {
		driver.quit();
	}

	@Test
	public void testCreateNewToDo() throws JSONException {
		String id = postUser("name", "example@example.com", 2);
		driver.get(baseUrl);
		driver.findElement(By.cssSelector("a[href*='/?db=2']")).click();
		driver.findElement(By.cssSelector("a[href*='/todo/ofuser/" + id + "?db=2']")).click();
		driver.findElement(By.cssSelector("a[href*='/todo/new/" + id + "?db=2']")).click();
		driver.findElement(By.name("date")).sendKeys("10102008");

		driver.findElement(By.name("key")).sendKeys("pippo");
		driver.findElements(By.name("value")).stream().filter(x -> x.getAttribute("value").contains("false"))
				.findFirst().get().click();
		driver.findElement(By.name("btn_add")).click();
		driver.findElement(By.name("key")).sendKeys("pluto");
		driver.findElements(By.name("value")).stream().filter(x -> x.getAttribute("value").contains("true")).findFirst()
				.get().click();
		driver.findElement(By.name("btn_submit")).click();
		assertThat(driver.findElement(By.id("todo_table")).getText()).contains("pippo=false", "pluto=true",
				"2008-10-10T00:00");
	}

	@Test
	public void testEditToDo() throws JSONException, JsonProcessingException {
		String idOfUser = postUser("name", "example@example.com", 1);
		ObjectMapper objMapper = new ObjectMapper();
		objMapper.registerModule(new JavaTimeModule());
		objMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		HashMap<String, Boolean> actions = new HashMap<>();
		actions.put("prova", true);
		actions.put("prova2", false);
		ToDoDTO todo = new ToDoDTO(null, Long.parseLong(idOfUser), actions, LocalDateTime.of(2000, 5, 13, 1, 1, 1));
		String idTodo = postToDo(objMapper.writeValueAsString(todo), 1);
		driver.get(baseUrl);
		driver.findElement(By.cssSelector("a[href*='/todo/ofuser/" + idOfUser + "']")).click();
		driver.findElement(By.cssSelector("a[href*='/todo/edit/" + idTodo + "']")).click();
		driver.findElement(By.name("date")).sendKeys("10102008");

		driver.findElements(By.name("actions[prova]")).stream().filter(x -> x.getAttribute("value").contains("false"))
				.findFirst().get().click();
		driver.findElements(By.name("actions[prova2]")).stream().filter(x -> x.getAttribute("value").contains("true"))
				.findFirst().get().click();
		driver.findElement(By.name("key")).sendKeys("pluto");
		driver.findElements(By.name("value")).stream().filter(x -> x.getAttribute("value").contains("true")).findFirst()
				.get().click();
		driver.findElement(By.name("btn_submit")).click();
		assertThat(driver.findElement(By.id("todo_table")).getText()).contains("prova=false", "prova2=true",
				"pluto=true", "2008-10-10T01:01:01");
	}

	private String postUser(String name, String email, int db) throws JSONException {
		JSONObject body = new JSONObject();
		body.put("name", name);
		body.put("email", email);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
		ResponseEntity<String> answer = new RestTemplate().postForEntity(baseUrl + "/api/users/" + db + "/new", entity,
				String.class);
		Logger logger = Logger.getLogger(ToDoWebControllerE2E.class.toString());
		logger.log(Level.INFO, "answer for POST: " + answer);
		return new JSONObject(answer.getBody()).get("id").toString();
	}

	private String postToDo(String body, int db) throws JSONException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(body, headers);
		ResponseEntity<String> answer = new RestTemplate().postForEntity(baseUrl + "/api/todo/" + db + "/new", entity,
				String.class);
		Logger logger = Logger.getLogger(ToDoWebControllerE2E.class.toString());
		logger.log(Level.INFO, "answer for POST: " + answer);
		return new JSONObject(answer.getBody()).get("id").toString();
	}

}