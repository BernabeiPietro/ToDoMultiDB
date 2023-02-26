package com.example.todoappmultidb.webcontroller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.todoappmultidb.model.dto.UserDTO;
import com.example.todoappmultidb.service.UserService;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserWebController.class)
public class UserWebControllerHtmlUnitTest {
	@Autowired
	private WebClient webClient;
	@MockBean
	private UserService userService;

	@Test
	public void testHomePageTitle() throws Exception {
		HtmlPage page = webClient.getPage("/");
		assertThat(page.getTitleText()).isEqualTo("Users");
	}

	@Test
	public void testHomePageWithNoUser() throws Exception {
		when(userService.getAllUser()).thenReturn(emptyList());
		HtmlPage page = this.webClient.getPage("/");
		assertThat(page.getBody().getTextContent()).contains("No user");
	}

	@Test
	public void test_HomePageWithUsers_ShowInATable_db_default() throws Exception {
		when(userService.getAllUser())
				.thenReturn(asList(new UserDTO(1L, "nome1", "email1"), new UserDTO(2L, "nome2", "email2")));
		HtmlPage page = this.webClient.getPage("/");
		assertThat(page.getBody().getTextContent()).doesNotContain("No user");
		HtmlTable table = page.getHtmlElementById("user_table");
		assertThat(table.asText()).isEqualTo("Users\n" + "ID	Name	Email\n"
				+ "1	nome1	email1	Edit 	Show-ToDo\n" + "2	nome2	email2	Edit 	Show-ToDo");
		page.getAnchorByHref("/user/edit/1");
		page.getAnchorByHref("/user/edit/2");
		page.getAnchorByHref("/todo/ofuser/1");
		page.getAnchorByHref("/todo/ofuser/2");
	}

	@Test
	public void test_HomePageWithUsers_db1_setRequestParameter() throws Exception {
		when(userService.getAllUser())
				.thenReturn(asList(new UserDTO(1L, "nome1", "email1"), new UserDTO(2L, "nome2", "email2")));
		HtmlPage page = this.webClient.getPage("/?db=1");
		HtmlTable table = page.getHtmlElementById("user_table");
		assertThat(table.asText()).isEqualTo("Users\n" + "ID	Name	Email\n"
				+ "1	nome1	email1	Edit 	Show-ToDo\n" + "2	nome2	email2	Edit 	Show-ToDo");
		page.getAnchorByHref("/user/edit/1?db=1");
		page.getAnchorByHref("/user/edit/2?db=1");
		page.getAnchorByHref("/todo/ofuser/1?db=1");
		page.getAnchorByHref("/todo/ofuser/2?db=1");
	}

	@Test
	public void test_HomePageWithUsers_db2_setRequestParameter() throws Exception {
		when(userService.getAllUser())
				.thenReturn(asList(new UserDTO(1L, "nome1", "email1"), new UserDTO(2L, "nome2", "email2")));
		HtmlPage page = this.webClient.getPage("/?db=2");
		HtmlTable table = page.getHtmlElementById("user_table");
		assertThat(table.asText()).isEqualTo("Users\n" + "ID	Name	Email\n"
				+ "1	nome1	email1	Edit 	Show-ToDo\n" + "2	nome2	email2	Edit 	Show-ToDo");
		page.getAnchorByHref("/user/edit/1?db=2");
		page.getAnchorByHref("/user/edit/2?db=2");
		page.getAnchorByHref("/todo/ofuser/1?db=2");
		page.getAnchorByHref("/todo/ofuser/2?db=2");
	}

	@Test
	public void test_HomePage_ShouldProvideALinkForCreatingANewUser_withoutRequestParam() throws Exception {
		HtmlPage page = this.webClient.getPage("/");
		assertThat(page.getAnchorByText("New user").getHrefAttribute()).isEqualTo("/user/new");
	}

	@Test
	public void test_HomePage_ShouldProvideALinkForCreatingANewUser_DB1() throws Exception {
		HtmlPage page = this.webClient.getPage("/?db=1");
		assertThat(page.getAnchorByText("New user").getHrefAttribute()).isEqualTo("/user/new?db=1");
	}

	@Test
	public void test_HomePage_ShouldProvideALinkForCreatingANewUser_DB2() throws Exception {
		HtmlPage page = this.webClient.getPage("/?db=2");
		assertThat(page.getAnchorByText("New user").getHrefAttribute()).isEqualTo("/user/new?db=2");
	}

	@Test
	public void test_HomePage_dbDefault_db1_notDisplayed()
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlPage page = this.webClient.getPage("/");
		assertThat(page.getElementsByName("db1")).isEmpty();
	}

	@Test
	public void test_HomePage_dbDefault_db2_displayed()
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlPage page = this.webClient.getPage("/");
		assertThat(page.getElementById("db2"));
		HtmlButton b2 = page.getElementByName("db2");
		assertThat(b2.isDisplayed());
	}

	@Test
	public void test_HomePage_db1_db1_notdisplayed()
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlPage page = this.webClient.getPage("/?db=1");
		assertThat(page.getElementsByName("db1")).isEmpty();
	}

	@Test
	public void test_HomePage_db1_db2_displayed()
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlPage page = this.webClient.getPage("/?db=1");
		assertThat(page.getElementById("db2"));
		HtmlButton b2 = page.getElementByName("db2");
		assertThat(b2.isDisplayed());
	}

	@Test
	public void test_HomePage_db2_db1_displayed()
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlPage page = this.webClient.getPage("/?db=2");
		HtmlButton b1 = page.getElementByName("db1");
		assertThat(b1.isDisplayed());
	}

	@Test
	public void test_HomePage_db2_db2_notdisplayed()
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlPage page = this.webClient.getPage("/?db=2");
		assertThat(page.getElementsByName("db2")).isEmpty();
	}

	@Test
	public void testEditNonExistentUsers() throws Exception {
		when(userService.getUserById(1L)).thenReturn(null);
		HtmlPage page = this.webClient.getPage("/user/edit/1");
		assertThat(page.getBody().getTextContent()).contains("No user found with id: 1");
	}

	@Test
	public void testEditExistentUser() throws Exception {
		when(userService.getUserById(1)).thenReturn(new UserDTO(1L, "original name", "original email"));
		HtmlPage page = this.webClient.getPage("/user/edit/1");
		final HtmlForm form = page.getFormByName("user_form");
		form.getInputByValue("original name").setValueAttribute("modified name");
		form.getInputByValue("original email").setValueAttribute("modified email");
		form.getButtonByName("btn_submit").click();
		verify(userService).updateUserById(1L, new UserDTO(1L, "modified name", "modified email"));
	}

	@Test
	public void testEditUser_db_1() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(userService.getUserById(1)).thenReturn(new UserDTO(1L, "original name", "original email"));
		HtmlPage page = this.webClient.getPage("/user/edit/1?db=1");
		final HtmlForm form = page.getFormByName("user_form");
		assertThat(form.getActionAttribute()).isEqualTo("/user/save?db=1");

	}

	@Test
	public void testEditUser_db_2() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(userService.getUserById(1)).thenReturn(new UserDTO(1L, "original name", "original email"));
		HtmlPage page = this.webClient.getPage("/user/edit/1?db=2");
		final HtmlForm form = page.getFormByName("user_form");
		assertThat(form.getActionAttribute()).isEqualTo("/user/save?db=2");

	}

	@Test
	public void testEditUser_db_default() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(userService.getUserById(1)).thenReturn(new UserDTO(1L, "original name", "original email"));
		HtmlPage page = this.webClient.getPage("/user/edit/1");
		final HtmlForm form = page.getFormByName("user_form");
		assertThat(form.getActionAttribute()).isEqualTo("/user/save");

	}

	@Test
	public void testEditNewUser() throws Exception {
		HtmlPage page = this.webClient.getPage("/user/new");
		final HtmlForm form = page.getFormByName("user_form");
		form.getInputByName("name").setValueAttribute("new name");
		form.getInputByName("email").setValueAttribute("new email");
		form.getButtonByName("btn_submit").click();
		verify(userService).insertNewUser(new UserDTO(null, "new name", "new email"));
	}

}
