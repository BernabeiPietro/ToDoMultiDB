package com.example.todoappmultidb.webcontroller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.todoappmultidb.model.User;
import com.example.todoappmultidb.repository.UserRepository;
import com.example.todoappmultidb.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserWebControllerServiceIT {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;
	@LocalServerPort
	private int port;
	private WebDriver driver;
	private String baseUrl;

	@Before
	public void setUp() throws Exception {
		baseUrl = "http://localhost:" + port;
		driver = new HtmlUnitDriver();

		userService.setDatabase(1);
		userRepository.deleteAll();
		userRepository.flush();
		userService.setDatabase(2);
		userRepository.deleteAll();
		userRepository.flush();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

	@Test
	public void testHomePage() {
		userService.setDatabase(1);
		User user = userRepository.save(new User(null, "test", "test"));
		driver.get(baseUrl);
		assertThat(driver.findElement(By.id("user_table")).getText()).contains(user.getId().toString(), "test", "test",
				"Edit", "Show-ToDo");
		driver.findElement(By.cssSelector("a[href*='/user/edit/" + user.getId() + "']"));
		driver.findElement(By.cssSelector("a[href*='/todo/ofuser/" + user.getId() + "']"));
		driver.findElement(By.cssSelector("a[href*='/user/new']"));
		driver.findElement(By.cssSelector("a[href*='/?db=2']"));
	}

	@Test
	public void testEditPageNewUser() throws Exception {
		driver.get(baseUrl + "/user/new");
		driver.findElement(By.name("name")).sendKeys("prova");
		driver.findElement(By.name("email")).sendKeys("prova");
		driver.findElement(By.name("btn_submit")).click();
		userService.setDatabase(1);
		assertThat(userRepository.findAll()).hasSize(1);
		assertThat(userRepository.findAll().get(0).getEmail()).isEqualTo("prova");
		assertThat(userRepository.findAll().get(0).getName()).isEqualTo("prova");

	}

	@Test
	public void testEditPageNewEmptyUser() throws Exception {
		driver.get(baseUrl + "/user/new");
		driver.findElement(By.name("name")).sendKeys("");
		driver.findElement(By.name("email")).sendKeys("");
		driver.findElement(By.name("btn_submit")).click();
		userService.setDatabase(1);
		assertThat(userRepository.findAll()).isEmpty();
		assertThat(driver.getPageSource()).contains("User with empty fields");

	}

	@Test
	public void testEditPageUpdateUser() throws Exception {
		userService.setDatabase(1);
		User user = userRepository.save(new User(null, "test", "test"));
		driver.get(baseUrl + "/user/edit/" + user.getId());
		final WebElement nameField = driver.findElement(By.name("name"));
		nameField.clear();
		nameField.sendKeys("new name");
		final WebElement emailField = driver.findElement(By.name("email"));
		emailField.clear();
		emailField.sendKeys("new email");
		driver.findElement(By.name("btn_submit")).click();
		assertThat(userRepository.findById(user.getId()).get().getName()).isEqualTo("new name");
		assertThat(userRepository.findById(user.getId()).get().getEmail()).isEqualTo("new email");
	}
}
