package com.example.todoappmultidb.webcontroller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.todoappmultidb.model.ToDo;
import com.example.todoappmultidb.model.User;
import com.example.todoappmultidb.repository.ToDoRepository;
import com.example.todoappmultidb.repository.UserRepository;
import com.example.todoappmultidb.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ToDoWebControllerServiceIT {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ToDoRepository todoRepository;

	@Autowired
	private UserService userService;
	@LocalServerPort
	private int port;
	private WebDriver driver;
	private String baseUrl;

	@Before
	public void setUp() throws Exception {
		baseUrl = "http://localhost:" + port + "/todo";
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
		ToDo todo = todoRepository
				.save(new ToDo(null, user, new HashMap<String, Boolean>(), LocalDateTime.of(2005, 3, 4, 0, 0)));
		driver.get(baseUrl+"/ofuser/"+user.getId());
		Logger logger=Logger.getLogger(ToDoWebControllerServiceIT.class.toString());
		logger.log(Level.ERROR, driver.getPageSource().toString());
		assertThat(driver.findElement(By.id("todo_table")).getText()).contains(todo.getId().toString(), LocalDateTime.of(2005, 3, 4, 0, 0).toString(),
				"Edit");
		driver.findElement(By.cssSelector("a[href*='/todo/new/"+user.getId() + "']"));
		driver.findElement(By.cssSelector("a[href*='/todo/edit/" + todo.getId() + "']"));
	}
	
}
