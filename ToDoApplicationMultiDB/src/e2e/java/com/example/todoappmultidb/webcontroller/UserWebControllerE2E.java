package com.example.todoappmultidb.webcontroller;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import io.github.bonigarcia.wdm.WebDriverManager;

public class UserWebControllerE2E {// NOSONAR
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
	public void testHomePage() {
		driver.get(baseUrl);
		assertThat(driver.findElement(By.cssSelector("a[href*='/user/new")));
	}

	@Test
	public void testCreateNewUser() {
		driver.get(baseUrl);
		driver.findElement(By.cssSelector("a[href*='/user/new")).click();
		driver.findElement(By.name("name")).sendKeys("pippo");
		driver.findElement(By.name("email")).sendKeys("example@example.com");
		driver.findElement(By.name("btn_submit")).click();
		assertThat(driver.findElement(By.id("user_table")).getText()).contains("pippo", "example@example.com");
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
		Logger logger = Logger.getLogger(UserWebControllerE2E.class.toString());
		logger.log(Level.INFO, "answer for POST: " + answer);
		return new JSONObject(answer.getBody()).get("id").toString();
	}

	@Test
	public void testEditUser_db2() throws JSONException {
		String id = postUser("name", "example@example.com", 2);
		driver.get(baseUrl);
		driver.findElement(By.cssSelector("a[href*='/?db=2']")).click();
		driver.findElement(By.cssSelector("a[href*='/user/edit/" + id + "']")).click();
		final WebElement nameField = driver.findElement(By.name("name"));
		nameField.clear();
		nameField.sendKeys("modified name");
		final WebElement emailField = driver.findElement(By.name("email"));
		emailField.clear();
		emailField.sendKeys("example.name@example.com");
		driver.findElement(By.name("btn_submit")).click();
		assertThat(driver.findElement(By.id("user_table")).getText()).contains(id, "modified name",
				"example.name@example.com");
	}

}
