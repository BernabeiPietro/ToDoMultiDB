package com.example.todoappmultidb.webcontroller;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.todoappmultidb.model.dto.ToDoDTO;
import com.example.todoappmultidb.service.ToDoService;
import com.example.todoappmultidb.service.UserService;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

import javassist.NotFoundException;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ToDoWebController.class)
public class ToDoWebControllerHtmlUnitTest {
	@Autowired
	private WebClient webClient;
	@MockBean
	private ToDoService todoService;

	@MockBean
	private UserService userService;

	// index of todo
	@Test
	public void test_ToDoListOfUserPage_WithNoToDo() throws Exception {
		when(userService.getToDoOfUser(1l)).thenThrow(new NotFoundException("Not found any Todo for You"));
		HtmlPage page = this.webClient.getPage("/todo/ofuser/1");
		assertThat(page.getBody().getTextContent()).contains("Not found any Todo for You");
	}

	@Test
	public void test_ToDoListOfUserPage_WithToDo_EmptyMap() throws Exception {
		when(userService.getToDoOfUser(1l))
				.thenReturn(asList(new ToDoDTO(1l, 1l, new HashMap<>(), LocalDateTime.of(2005, 5, 1, 0, 0)),
						new ToDoDTO(2l, 1l, new HashMap<>(), LocalDateTime.of(2017, 5, 1, 0, 0))));
		HtmlPage page = this.webClient.getPage("/todo/ofuser/1");
		assertThat(page.getBody().getTextContent()).doesNotContain("Not found any Todo for You");
		HtmlTable table = page.getHtmlElementById("todo_table");
		assertThat(table.asText()).isEqualTo("ToDo\n" + "ID	Actions	Data\n" + "1	 	2005-05-01T00:00	Edit\n"
				+ "2	 	2017-05-01T00:00	Edit");

	}

	@Test
	public void test_ToDoListOfUserPage_WithToDo_FullMap() throws Exception {
		HashMap<String, Boolean> actions = new HashMap<>();
		actions.put("first", true);
		actions.put("second", true);
		when(userService.getToDoOfUser(1l))
				.thenReturn(asList(new ToDoDTO(1l, 1l, actions, LocalDateTime.of(2005, 5, 1, 0, 0)),
						new ToDoDTO(2l, 1l, actions, LocalDateTime.of(2017, 5, 1, 0, 0))));
		HtmlPage page = this.webClient.getPage("/todo/ofuser/1");
		assertThat(page.getBody().getTextContent()).doesNotContain("Not found any Todo for You");
		HtmlTable table = page.getHtmlElementById("todo_table");
		assertThat(table.asText()).isEqualTo(
				"ToDo\n" + "ID	Actions	Data\n" + "1	 first=true second=true 	2005-05-01T00:00	Edit\n"
						+ "2	 first=true second=true 	2017-05-01T00:00	Edit");
	}

	// edit todo
	@Test
	public void test_EditToDoWhenNotExist() throws Exception {
		when(todoService.findByIdDTO(1L)).thenThrow(new NotFoundException("Not found any Todo with id 1"));
		HtmlPage page = this.webClient.getPage("/todo/edit/1");
		assertThat(page.getBody().getTextContent()).contains("Not found any Todo with id 1");
	}

	@Test
	public void test_EditExistentTodo_emptyMap() throws Exception {
		when(todoService.findByIdDTO(1L))
				.thenReturn(new ToDoDTO(1l, 1l, new HashMap<>(), LocalDateTime.of(2005, 5, 1, 0, 0)));
		HtmlPage page = this.webClient.getPage("/todo/edit/1");
		final HtmlForm form = page.getFormByName("todo_form");
		form.getInputByValue("2005-05-01 00:00:00")
				.setValueAttribute(LocalDateTime.of(2007, 06, 01, 00, 00).toString());
		form.getButtonByName("btn_submit").click();
		verify(todoService).updateById(1l,
				new ToDoDTO(1l, 1l, new HashMap<>(), LocalDateTime.of(2007, 06, 01, 00, 00)));
	}

	@Test
	public void test_EditTodo_dbDefault() throws Exception {
		when(todoService.findByIdDTO(1L))
				.thenReturn(new ToDoDTO(1l, 1l, new HashMap<>(), LocalDateTime.of(2005, 5, 1, 0, 0)));
		HtmlPage page = this.webClient.getPage("/todo/edit/1");
		final HtmlForm form = page.getFormByName("todo_form");
		assertThat(form.getActionAttribute()).isEqualTo("/todo/save");
		assertThat(form.getButtonByName("btn_add").getAttribute("formaction")).isEqualTo("/todo/addaction");
	}

	@Test
	public void test_EditTodo_db1() throws Exception {
		when(todoService.findByIdDTO(1L))
				.thenReturn(new ToDoDTO(1l, 1l, new HashMap<>(), LocalDateTime.of(2005, 5, 1, 0, 0)));
		HtmlPage page = this.webClient.getPage("/todo/edit/1?db=1");
		final HtmlForm form = page.getFormByName("todo_form");
		assertThat(form.getActionAttribute()).isEqualTo("/todo/save?db=1");
		assertThat(form.getButtonByName("btn_add").getAttribute("formaction")).isEqualTo("/todo/addaction?db=1");

	}

	@Test
	public void test_EditTodo_db2() throws Exception {
		when(todoService.findByIdDTO(1L))
				.thenReturn(new ToDoDTO(1l, 1l, new HashMap<>(), LocalDateTime.of(2005, 5, 1, 0, 0)));
		HtmlPage page = this.webClient.getPage("/todo/edit/1?db=2");
		final HtmlForm form = page.getFormByName("todo_form");
		assertThat(form.getActionAttribute()).isEqualTo("/todo/save?db=2");
		assertThat(form.getButtonByName("btn_add").getAttribute("formaction")).isEqualTo("/todo/addaction?db=2");

	}

	@Test
	public void test_EditExistentTodo() throws Exception {
		HashMap<String, Boolean> actions = new HashMap<>();
		actions.put("first", true);
		actions.put("second", true);
		when(todoService.findByIdDTO(1L)).thenReturn(new ToDoDTO(1l, 1l, actions, LocalDateTime.of(2005, 5, 1, 0, 0)));
		HtmlPage page = this.webClient.getPage("/todo/edit/1");
		final HtmlForm form = page.getFormByName("todo_form");
		form.getInputByValue("2005-05-01 00:00:00")
				.setValueAttribute(LocalDateTime.of(2007, 06, 01, 00, 00).toString());
		form.getInputByName("actions[first]").setValueAttribute("false");
		form.getInputByName("actions[second]").setValueAttribute("false");
		form.getButtonByName("btn_submit").click();
		actions.put("first", false);
		actions.put("second", false);
		verify(todoService).updateById(1l, new ToDoDTO(1l, 1l, actions, LocalDateTime.of(2007, 06, 01, 00, 00)));
	}

	@Test
	public void test_EditExistentToDo_addAction() throws Exception {
		HashMap<String, Boolean> actions = new HashMap<>();
		actions.put("first", true);
		actions.put("second", true);
		when(todoService.findByIdDTO(1L)).thenReturn(new ToDoDTO(1l, 1l, actions, LocalDateTime.of(2005, 5, 1, 0, 0)));
		HtmlPage page = this.webClient.getPage("/todo/edit/1");
		final HtmlForm form = page.getFormByName("todo_form");
		form.getInputByName("date").setValueAttribute(LocalDateTime.of(2007, 06, 01, 00, 00).toString());
		form.getInputByName("key").setValueAttribute("third");
		form.getInputByName("value").setValueAttribute("false");
		form.getButtonByName("btn_add").click();
		verify(todoService).findByIdDTO(1l);
		verifyNoMoreInteractions(todoService);

	}

	// new todo
	@Test
	public void test_EditNewToDo_addAction() throws Exception {
		HashMap<String, Boolean> actions = new HashMap<>();
		actions.put("first", true);
		HtmlPage page = this.webClient.getPage("/todo/new/1");
		final HtmlForm form = page.getFormByName("todo_form");
		form.getInputByName("date").setValueAttribute(LocalDateTime.of(2007, 06, 01, 00, 00).toString());
		form.getInputByName("key").setValueAttribute("first");
		form.getInputByName("value").setValueAttribute("false");
		form.getButtonByName("btn_add").click();
		verifyNoInteractions(todoService);
		verifyNoInteractions(userService);

	}

	@Test
	public void test_EditNewToDo_dbDefault() throws Exception {
		HashMap<String, Boolean> actions = new HashMap<>();
		actions.put("first", true);
		HtmlPage page = this.webClient.getPage("/todo/new/1");
		final HtmlForm form = page.getFormByName("todo_form");
		assertThat(form.getActionAttribute()).isEqualTo("/todo/save");
		assertThat(form.getButtonByName("btn_add").getAttribute("formaction")).isEqualTo("/todo/addaction");

	}

	@Test
	public void test_EditNewToDo_db1() throws Exception {
		HashMap<String, Boolean> actions = new HashMap<>();
		actions.put("first", true);
		HtmlPage page = this.webClient.getPage("/todo/new/1?db=1");
		final HtmlForm form = page.getFormByName("todo_form");
		assertThat(form.getActionAttribute()).isEqualTo("/todo/save?db=1");
		assertThat(form.getButtonByName("btn_add").getAttribute("formaction")).isEqualTo("/todo/addaction?db=1");

	}

	@Test
	public void test_EditNewToDo_db2() throws Exception {
		HashMap<String, Boolean> actions = new HashMap<>();
		actions.put("first", true);
		HtmlPage page = this.webClient.getPage("/todo/new/1?db=2");
		final HtmlForm form = page.getFormByName("todo_form");
		assertThat(form.getActionAttribute()).isEqualTo("/todo/save?db=2");
		assertThat(form.getButtonByName("btn_add").getAttribute("formaction")).isEqualTo("/todo/addaction?db=2");

	}

	// Test link
	@Test
	public void test_ToDoShow_EditLink() throws Exception {
		when(userService.getToDoOfUser(1l))
				.thenReturn(asList(new ToDoDTO(1l, 1l, new HashMap<>(), LocalDateTime.of(2005, 5, 1, 0, 0))));
		HtmlPage page = this.webClient.getPage("/todo/ofuser/1");
		assertThat(page.getAnchorByText("Edit").getHrefAttribute()).isEqualTo("/todo/edit/1");

	}

	@Test
	public void test_ToDoShow_EditLink_db2() throws Exception {
		when(userService.getToDoOfUser(1l))
				.thenReturn(asList(new ToDoDTO(1l, 1l, new HashMap<>(), LocalDateTime.of(2005, 5, 1, 0, 0))));
		HtmlPage page = this.webClient.getPage("/todo/ofuser/1?db=2");
		assertThat(page.getAnchorByText("Edit").getHrefAttribute()).isEqualTo("/todo/edit/1?db=2");

	}

	@Test
	public void test_ToDoShow_NewToDoLink() throws Exception {
		HtmlPage page = this.webClient.getPage("/todo/ofuser/1");
		assertThat(page.getAnchorByText("New ToDo").getHrefAttribute()).isEqualTo("/todo/new/1");
	}

	@Test
	public void test_ToDoShow_NewToDoLink_db1() throws Exception {
		HtmlPage page = this.webClient.getPage("/todo/ofuser/1?db=1");
		assertThat(page.getAnchorByText("New ToDo").getHrefAttribute()).isEqualTo("/todo/new/1?db=1");
	}

	@Test
	public void test_ToDoShow_NewToDoLink_db2() throws Exception {
		HtmlPage page = this.webClient.getPage("/todo/ofuser/1?db=2");
		assertThat(page.getAnchorByText("New ToDo").getHrefAttribute()).isEqualTo("/todo/new/1?db=2");
	}

	@Test
	public void test_ToDoShow_returnToHome() throws Exception {
		HtmlPage page = this.webClient.getPage("/todo/ofuser/1");
		assertThat(page.getAnchorByText("Home").getHrefAttribute()).isEqualTo("/");
	}

	@Test
	public void test_ToDoEdit_returnToHome() throws Exception {
		when(todoService.findByIdDTO(1L))
				.thenReturn(new ToDoDTO(1l, 1l, new HashMap<>(), LocalDateTime.of(2005, 5, 1, 0, 0)));
		HtmlPage page = this.webClient.getPage("/todo/edit/1");
		assertThat(page.getAnchorByText("Home").getHrefAttribute()).isEqualTo("/");
	}

	@Test
	public void test_ToDoNew_returnToHome() throws Exception {
		HtmlPage page = this.webClient.getPage("/todo/new/1");
		assertThat(page.getAnchorByText("Home").getHrefAttribute()).isEqualTo("/");
	}

}
